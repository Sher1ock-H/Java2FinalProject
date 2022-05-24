# CS209A Final Project——Github热门java项目排名与分析

成员： 12012403 卞睿冬  12012427 黄柯睿

## Overview

​		本次项目的主题是，GitHub最热门的java项目的排序，以及对这些项目的建立时间、描述中的关键词进行分析。

​		本次项目一共分为三大模块，分别是：

1. ​	*GitHub*中*watcher*数最多的前1000个*java*仓库建立时间分布

2. ​    *GitHub*中*watcher*数最多的前1000个*java*仓库详细信息

3. ​    *GitHub*中*watcher*数最多的前1000个*java*仓库关键词展示

   项目架构：*待补充*

## 项目背景

​        我们使用GitHub搜索一些感兴趣的仓库时，我们经常会按照不同的方法排序，以找到我们想找的那个仓库。但是GitHub自带的搜索除了Best match外，只提供了按照stars数、forks数以及更新时间的排序。<img align="right" src="D:\Documents\CS209A Final Project——.assets\image-20220523214326301.png" alt="image-20220523214326301" style="zoom:80%;" />

​        但是我们想知道，用户们对于java，最关心的项目有哪些，他们最关心的这些项目，都有什么共性，并且我们想知道，大家最关心的仓库一般是在什么时间段建立的。这个时候用我们就要根据仓库的watcher数进行排序，找出用户最关心的java项目是哪些，而这是GitHub没有直接提供的，需要我们自己实现。所以，我们运用了GitHub的api，将返回的数据按照watcher数排序。返回后，在后端对仓库信息进行筛选、合并和分割处理，存储到info.json文件中保存于本地，并且预留了给前端获取的接口，用于从本地读取数据并返回给前端界面。同时还做了按关注数来对这些热门java项目排行的搜索页面，解决GitHub无法根据watcher数排序的问题。





## 数据收集

​        我们需要收集的数据是：GitHub中以java为主要语言的watcher数最多的前1000个仓库。我们收集的方式是：用GitHub提供的api爬取。由于GitHub的api单次访问只能返回100条数据，所以我们使用了for循环，一共读取了10页的数据，总共收集了1000条。[https://api.github.com/search/repositories?q=language:java+created:>2010+stars:>800&sort=watchers&per_page=100](https://api.github.com/search/repositories?q=language:java+created:>2010+stars:>800&sort=watchers&per_page=100)，在后面加上&page=i，可以让api返回第i页的内容。在本次项目中，我们只考虑2010年之后的建立的仓库，因为在2010年往前的仓库会有很多问题已经不是现在的人们所关心的了，但是以前的watcher数仍然存在。同时，若直接不加限制条件地爬取，因为数据量过大，"incomplete_results"返回值为true，将会丢失部分的仓库内容，对watcher数的排序也会出问题。对于这个问题，我们的解决方法是，在查询时添加star数的限制条件，虽然star数和watcher数之间并不存在直接的关系，不同仓库的这两个值也天差地别，不过对于人们最关注的仓库，他的收藏量肯定也不会低。经过多次尝试，我们决定把临界值定为800，只爬取收藏量大于800的仓库，从而使返回值完整。

## 数据存储

​        本次项目所有的数据，全部存放于src/main/java/com/Java2Project/dataSet文件夹下。所有收集的数据，经过筛选和加工后，全部存放于info.json文件里。在数据存储的过程中，主要用到了两个库，一个是apache的comment子项目中的Beanutils包，用于将收集到的json数据读取为字符串，另一个是Gson库，用于生成info.json文件。保存文件用的是BufferWriter方法。在生成json文件时，我们写了两个内部类来构造json文件。

```java
public static class ItemArray {
        public ArrayList<Item> items;

        public ItemArray() {
            items = new ArrayList<>();
        }

        public void addItem(Item item) {
            this.items.add(item);
        }

        public void sort() {
            items.sort(Comparator.comparingInt(i -> -i.watchers));
            //items.sort((a, b) -> a.watchers - b.watchers);
        }
    }

    public static class Item {
        public String name;
        public String description;
        public int year;
        public int month;
        public int day;
        public int watchers;
        public String url;
        public int id;

        public Item(String name, String description, int year, int month, int day, int watchers, String url, int id) {
            this.name = name;
            this.description = description;
            this.year = year;
            this.month = month;
            this.day = day;
            this.watchers = watchers;
            this.url = url;
            this.id = id;
        }
    }
```

​        <img align="right" src="D:\Documents\CS209A Final Project——.assets\image-20220524130446348.png" alt="image-20220524130446348"  />此外，除所有收集到的数据info.json外，在dataSet中我们还存储了两个数据，frequency.json是用于生成频率饼图的数据，以及从网上下载的中英文stop words，用于筛掉nlp分割出的关键词中无意义的成分。前端调用时可以直接调用在这里面的数据而不需要重新爬取，增强了稳定性。



## 数据处理

​        在这一步，我们要将筛选整合过的数据进行进一步的处理。

​        在前两个模块中，数据处理主要是对数据的筛选和统计，GitHub的api返回的数据十分庞杂，我们要在其中挑选出对分析时间规律有意义的信息。在存储数据时只将仓库名称、描述、url、建立时间存储下来，而对于建立时间，我们用字符串处理将其分割为年月日。在进行柱状图展示时，在前端将返回的年月以逐条导入的方式进行统计及展示。

<img align = "right" src="D:\Documents\CS209A Final Project——.assets\image-20220524140845510.png" alt="image-20220524140845510"  />在第三个模块中，我们用到了kumo项目中的*FrequencyAnalyzer*，用库中的nlp技术自动分割仓库描述中的关键词以及统计这些关键词出现的频率。对于英文单词，采取的是直接以单词分割的形式，对于中文的句子，*FrequencyAnalyzer*引入了中文词库，用其进行分词，分词后将词语和出现频率记录在链表`List<WordFrequency>`里。在这些词语中有很多无意义的连接词，于是我们在GitHub中找了中文的停用词表和英文的stop words，保存在stopwords.txt中，筛掉含有这些停用词的单词数据。最后以json文件的形式，将分析出的数据存在frequency.json中，用于第三个模块中生成关键词饼图。右图是经过分词处理后出现频率最高的7个关键词。





## 数据展示

​        *这个你先吹，可以多贴点图，记得重要的地方写清楚自己是怎么做的，用的什么框架，我到时候补充*

## 数据的分析与理解

​        在生成的热门仓库时间分布图中我们可以看出来，关注量最大的几个项目集中在2018年前后，而前1000个项目里，2015、2016两年创建的项目占到了大多数。而2012年以前，和2019年之后创建的仓库，在热门仓库里的次数都较少且逐年递减。我们认为，这是因为在2012年以前的项目大多过于陈旧或停止了维护，减少了用户的关注度，且Java版本也比较旧。而2015年前后创建的仓库，因其有相对较新的主题以及存在了较长的时间，因此占了热门仓库的很大一部分，并且我们注意到，在2014年，Java8发布，引入了包括lambda、流处理在内的很多新特性，所以这些项目的火热很可能与之有关。2018年前后创建的仓库因为有新颖的主题和极大的实用性（比如JavaGuide、leetcodeAnimation），因此会有很多人关注这种项目。而近年的项目因为时间距离现在较短，完成度不高，所以watcher数会比之前创建的仓库少。对于仓库创建的月份，则与热门程度无关。不过从2018年以来，12月建立的仓库watcher数高的普遍较少，可能是因为java新版本的发布一般是在3月和9月，而12月时没有新版本的发布。所以我们分析出，这些Java热门项目与新版本的发布是有一定相关性的。

<img src="D:\Documents\CS209A Final Project——.assets\image-20220524145221693.png" alt="image-20220524145221693" style="zoom: 50%;" />

​        对于java项目描述中的关键词进行分析，我们可以发现，android一词在描述里出现的频率最高，在1000个仓库里，出现了372次，我们可以看出，在安卓上的应用是人们比较关心的一个方面，同时，java也是安卓app开发的首选语言。在中文关键词里，最突出的是支持、系统、管理三个词。说明人们关心的Java项目有很多都是用于有关系统的开发，和对资源等的管理。

## 总结

​        通过这次项目，*待补充*