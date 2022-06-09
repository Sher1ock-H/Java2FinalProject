# CS209A Final Project——Github热门java项目排名与分析

成员： 12012403 卞睿冬  12012427 黄柯睿

## Overview

​		本次项目的主题是，GitHub最热门的java项目的排序，以及对这些项目的建立时间、描述中的关键词进行分析。

​		本次项目一共分为三大模块，分别是：

1. ​	*GitHub*中*watcher*数最多的前1000个*java*仓库建立时间分布

2. ​    *GitHub*中*watcher*数最多的前1000个*java*仓库详细信息

3. ​    *GitHub*中*watcher*数最多的前1000个*java*仓库关键词展示

   项目架构：我们的项目采用的是B/S架构![image-20220524203607011](Java2Report.assets\image-20220524203607011.png)

## 项目背景

​        我们使用GitHub搜索一些感兴趣的仓库时，我们经常会按照不同的方法排序，以找到我们想找的那个仓库。但是GitHub自带的搜索除了Best match外，只提供了按照stars数、forks数以及更新时间的排序。<img align="right" src="CS209A Final Project——.assets\image-20220523214326301.png" alt="image-20220523214326301" style="zoom:80%;" />

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

​        <img align="right" src="CS209A Final Project——.assets\image-20220524130446348.png" alt="image-20220524130446348"  />此外，除所有收集到的数据info.json外，在dataSet中我们还存储了两个数据，frequency.json是用于生成频率饼图的数据，以及从网上下载的中英文stop words，用于筛掉nlp分割出的关键词中无意义的成分。前端调用时可以直接调用在这里面的数据而不需要重新爬取，增强了稳定性。



## 数据处理

​        在这一步，我们要将筛选整合过的数据进行进一步的处理。

​        在前两个模块中，数据处理主要是对数据的筛选和统计，GitHub的api返回的数据十分庞杂，我们要在其中挑选出对分析时间规律有意义的信息。在存储数据时只将仓库名称、描述、url、建立时间存储下来，而对于建立时间，我们用字符串处理将其分割为年月日。在进行柱状图展示时，在前端将返回的年月以逐条导入的方式进行统计及展示。

<img align = "right" src="CS209A Final Project——.assets\image-20220524140845510.png" alt="image-20220524140845510"  />在第三个模块中，我们用到了kumo项目中的*FrequencyAnalyzer*，用库中的nlp技术自动分割仓库描述中的关键词以及统计这些关键词出现的频率。对于英文单词，采取的是直接以单词分割的形式，对于中文的句子，*FrequencyAnalyzer*引入了中文词库，用其进行分词，分词后将词语和出现频率记录在链表`List<WordFrequency>`里。在这些词语中有很多无意义的连接词，于是我们在GitHub中找了中文的停用词表和英文的stop words，保存在stopwords.txt中，筛掉含有这些停用词的单词数据。最后以json文件的形式，将分析出的数据存在frequency.json中，用于第三个模块中生成关键词饼图。右图是经过分词处理后出现频率最高的7个关键词。





## 数据展示

### 后端部分

​		后端部分我们此次project用的框架是Springboot+thymeleaf，以下是依赖：

```xml
<dependencies>
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
   </dependency>
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
   </dependency>
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-thymeleaf</artifactId>
   </dependency>
</dependencies>
```

​		用springboot最大的优势是简化配置，它实现了自动化配置，不需要冗杂的配置流程，只需简单的代码就可以实现一个后端服务

### 前端部分

​		这次的数据展示我们用的语言是Html + CSS + JavaScript，我们设计了三个界面，分别为bar-race, pie-chart以及rank-list

​		所使用的框架有：

> Echarts.js	version: 5.3.2
>
> Bootstrap.css	version: 5.1.3
>
> Bootstrap.js	version: 5.1.3
>
> JQuery.min.js	version: 3.6.0

我们提供了本地的框架文件，在static目录下

同时，我们提供了CDN版本的框架文件：

```html
<link href="https://cdn.staticfile.org/bootstrap/5.1.3/css/bootstrap.css" rel="stylesheet">
<script src="https://cdn.staticfile.org/bootstrap/5.1.3/js/bootstrap.js"></script>
<script src="https://cdn.staticfile.org/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdn.staticfile.org/echarts/5.3.2/echarts.min.js"></script>
```

使用CDN的好处是：

- 许多用户在访问另一个站点时已经CDN节点下载了对应文件。因此，当他们访问我们的网站时，它将从缓存中加载，从而缩短加载时间。
- 此外，大多数 CDN 将确保一旦用户从中请求文件，它将从最接近他们的服务器提供服务，这也导致更快的加载时间。

#### Bar-Race

​		这部分的标题是：Github按Watchers排序前1000条的时间分布

​		我们制作了一个动态变化的柱状图，我们在10s内将第一个项目开始到第1000个项目逐渐加入到表中，可以看到各个柱子之间的竞争，最终停下来，我们可以看到各个年份的条目数量

<img src="CS209A Final Project——.assets/Github按Watchers排序前1000条的时间分布.png" alt="Github按Watchers排序前1000条的时间分布">

可以看出，2015年和2016年的数量最多，2021和2022年的条目最少，可以看出最近两年内的人气项目比较少

同时，我们点击某一年的柱子，我们可以进入那一年去看这一年哪个月的项目最多

<img src="CS209A Final Project——.assets\Github按Watchers排序前1000条的时间分布 Year = 2015.png" alt="Github按Watchers排序前1000条的时间分布 Year = 2015"  />

以2015为例，我们可以看到不同时间的项目数量，其中11月份的数量最多，5月的项目最少

#### Pie-Chart

​		这部分的标题是：Github仓库关键词分布图。

​		在这部分我们利用kumo包的nlp技术分离前1000条数据中每条数据的description部分，并选取排名最前的600个词语用于制作饼图，初始我们只放了前30条在上面，如果有那一条的词语不重要，可以选择点击将其隐藏掉，之后程序将自动添加下一条数据，并保持条数一直在30条，不多不少

​		通过对这段数据的分析，我们可以看出频率最高的词汇是android，并可以大致看出热门项目的大致类型

<img src="CS209A Final Project——.assets\Github仓库关键词分布图.png" alt="Github仓库关键词分布图"  />

#### Rank-List

​		在这部分我们写了一个简单的动态网页：排名列表，总共1000条的数据我们将它分成10页展示，每页100条数据。在加载好页面的时候向服务器请求第一页的数据，并封装成html语句加到网页里

```javascript
$(document).ready(function () {
    getPage(1);
})
function getPage(i) {
    fetch('page/' + i).then(response => response.json()).then(json => {
        dataSet = json['items'];
        setList();
    })
}
function setList() {
    $(".list-group").empty();
    let html;
    for (let i = 0; i < dataSet.length; i++) {
        html = '<li id="' + i + '" class="list-group-item" style="cursor: pointer" onclick="showDialog(this)">'
            + dataSet[i]['name']
            + '<span class="float-end text-secondary">' + dataSet[i]['watchers']
            + '</span>'
            + '</li>';
        $('#list').append(html);
    }
}
```

最终的效果大致是这样（左侧是项目名称，右侧是其Watcher数）：

<img src="CS209A Final Project——.assets\20220524195520.png" alt="20220524195520"  />

​		同时，我们还对每一条数据添加了点击事件，在点击某一条数据的时候会弹出模态框，里面会展示与该条数据有关的信息，包括仓库描述，仓库的watcher数以及仓库建立的时间，并且提供了“转到Github界面”的选项，效果大概如图：

<img src="CS209A Final Project——.assets\20220524200758.png" alt="20220524200758"  />

​		同时我们还提供了搜索方法，可以传入想要搜索的字符，将从服务器返回标题中含有该字符的相应结果，示例如下：

​		我们搜索Android，将产生以下界面：

<img src="CS209A Final Project——.assets\20220524201058.png" alt="20220524201058"  />

## 数据的分析与理解

​        在生成的热门仓库时间分布图中我们可以看出来，关注量最大的几个项目集中在2018年前后，而前1000个项目里，2015、2016两年创建的项目占到了大多数。而2012年以前，和2019年之后创建的仓库，在热门仓库里的次数都较少且逐年递减。我们认为，这是因为在2012年以前的项目大多过于陈旧或停止了维护，减少了用户的关注度，且Java版本也比较旧。而2015年前后创建的仓库，因其有相对较新的主题以及存在了较长的时间，因此占了热门仓库的很大一部分，并且我们注意到，在2014年，Java8发布，引入了包括lambda、流处理在内的很多新特性，所以这些项目的火热很可能与之有关。2018年前后创建的仓库因为有新颖的主题和极大的实用性（比如JavaGuide、leetcodeAnimation），因此会有很多人关注这种项目。而近年的项目因为时间距离现在较短，完成度不高，所以watcher数会比之前创建的仓库少。对于仓库创建的月份，则与热门程度无关。不过从2018年以来，12月建立的仓库watcher数高的普遍较少，可能是因为java新版本的发布一般是在3月和9月，而12月时没有新版本的发布。所以我们分析出，这些Java热门项目与新版本的发布是有一定相关性的。

<img src="CS209A Final Project——.assets\image-20220524145221693.png" alt="image-20220524145221693" style="zoom: 50%;" />

​        对于java项目描述中的关键词进行分析，我们可以发现，android一词在描述里出现的频率最高，在1000个仓库里，出现了372次，我们可以看出，在安卓上的应用是人们比较关心的一个方面，同时，java也是安卓app开发的首选语言。在中文关键词里，最突出的是支持、系统、管理三个词。说明人们关心的Java项目有很多都是用于有关系统的开发，和对资源等的管理。

## 总结

​        通过这次项目，我们发现了GitHub无法在网页上直接按关注数排序java仓库的问题，以及对java热门仓库的建立时间分布的好奇，通过数据收集与分析，成功解决了这一问题并做了网页展示，也通过数据分析和统计知道了仓库建立时间的分布规律，猜测了影响的要素。同时，这个项目也是我们大学以来第一个完成度较高的一个项目。在这个项目中，我们第一次将Java爬虫应用于实际情况中，也第一次接触了有关NLP的库和简单的使用。同时也学习了如何进行前后端的交互，学习了怎么利用框架来简化前端网页的构建。我们还从合作中学到了如何与队友交流，比如负责前端的同学把前端网页需要使用到的接口告诉负责后端的同学，而后者负责用Java实现相应接口并供前端的同学来调用，通过合适的任务分配和沟通交流，我们的项目开发快了许多。
