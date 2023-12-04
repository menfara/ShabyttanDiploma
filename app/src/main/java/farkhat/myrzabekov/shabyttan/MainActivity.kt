package farkhat.myrzabekov.shabyttan

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.LocaleList
import android.preference.PreferenceManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity
import farkhat.myrzabekov.shabyttan.databinding.ActivityMainBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.OnArtworkClickListener
import farkhat.myrzabekov.shabyttan.presentation.ui.fragments.ArtworkBottomSheetFragment
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnArtworkClickListener {
    private val viewModel: MainViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


//        viewModel.getArtworkByViewDate("29/11/2023")

//        viewModel.todayArtworkLiveData.observe(this) { artwork ->
//            binding.textView.text = artwork?.titleRu ?: "Artwork not found"
//            binding.textView.text =
//                (binding.textView.text.toString() +"\n"+ artwork?.descriptionRu) ?: "Artwork not found"
//        }



        createArtworks()



        setupNavigation()
        handleDeepLink(intent)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            val uri: Uri? = intent.data
            if (uri != null) {
                val id: String? = uri.lastPathSegment
                Log.d(">>> ", "ID: $id")
                if (id != null) {
                    onArtworkClick(id.toLong())
                }
            }
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as? NavHostFragment
            ?: return
        navController = navHostFragment.navController
        binding.bottomnavigation.setupWithNavController(navController)
    }


    private fun createArtworks() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val userId = sharedPreferences.getLong("userId", -1L)
        Log.d(">>> userId", userId.toString())
        if (userId != -1L) return

        viewModel.createUser(
            UserEntity(
                username = "Farkhat",
                email = "email@mail.com",
                password = "pass"
            )
        )

        viewModel.setUserId(1)
        viewModel.setUserLanguage()
        val artworksList = listOf(
            ArtworkEntity(
                title = "Hunting trophy",
                titleRu = "Охотничий трофей",

                creator = "Claude Monet",
                creatorRu = "Клод Моне",

                creationDate = "c. 1862",
                creationDateRu = "около 1862",

                technique = "Oil painting",
                techniqueRu = "Масляная живопись",

                type = "Landscape",
                typeRu = "Пейзаж",

                description = "At the very beginning of his career, Monet most often turned to the depiction of inanimate objects. Although he later continued to be interested in this genre, still life never became one of the main themes of his painting. This was explained by the fact that, unlike portrait subjects, still life was a less expensive field of study for young artists of the XIX century and did not require payment for the work of models. In any case, Monet firmly considered still lifes to be his best achievement, offering one of them in 1859 to the Municipal Council of Le Havre with a scholarship application. In the same year, he presented two still lifes to the painter Constant Troyon to demonstrate his painting skills. This work shows amazing skill, not expected from a young artist. Of course, the composition and the selected models trace the legacy of Chardin and the splendor of Troyon's canvases. In the Salon of 1859, Monet admired Troyon's painting depicting a dog with a partridge in its teeth: \"it's wonderful that you can smell its fur,\" he wrote to his mentor Boudin. But the confidence in the composition and the boldness in the interaction of textures show that Monet could already demonstrate his independence from the influence of recognized masters of this genre.",
                descriptionRu = "В самом начале своей карьеры Моне наиболее часто обращается к изображению неодушевленных предметов. Хотя впоследствии он продолжает интересоваться этим жанром, натюрморт так и не стал одной из главных тем его живописи. Это объяснялось тем, что натюрморт в отличие от портретной тематики был менее дорогостоящей областью исследования для молодых художников XIX века и не требовал платы за труд натурщиц. В любом случае, Моне твердо считал натюрморты своим лучшим достижением, предложив один из них в 1859 году в муниципальный совет Гавра с ходатайством о стипендии. В том же году он представил два натюрморта живописцу Констану Тройону для демонстрации своих навыков живописи. Данная работа показывает удивительное мастерство, не ожидаемое от юного художника. Конечно, в композиции и выбранных моделях прослеживается наследие Шардена и пышность полотен Тройона. В Салоне 1859 года Моне восхитила картина Тройона, изображающая собаку с куропаткой в зубах: \"это замечательно, что вы можете чувствовать запах ее шерсти\", писал он своему наставнику Будену. Но уверенность в композиции и смелость во взаимодействии текстур показывают, что Моне уже мог демонстрировать свою независимость от влияния признанных мастеров этого жанра.",

                didYouKnow = "Claude Monet's \"Hunting Trophy\" is a rare example of his early still lifes and demonstrates the artist's departure from traditional academicism to impressionism.",
                didYouKnowRu = "\"Охотничий трофей\" Клода Моне, является редким примером его ранних натюрмортов и демонстрирует отход художника от традиционного академизма к импрессионизму.",

                imageUrl = "https://impressionism.su/monet/picture/Hunting%20Trophy.jpg",
                viewDate = "2023/12/02"
            ),
            ArtworkEntity(
                title = "Terrace at Sainte-Adresse",
                titleRu = "Терраса в Сент-Адресс",

                creator = "Claude Monet",
                creatorRu = "Клод Моне",

                creationDate = "1867",
                creationDateRu = "1867",

                technique = "Oil painting",
                techniqueRu = "Масляная живопись",

                type = "Urban landscape",
                typeRu = "Пейзаж",

                description = "Two years before painting \"The Terrace at Saint-Adresse\" Monet wrote: \"I come up with amazing things when I go to Saint-Adresse.\" Indeed, the real work is an \"amazing thing\" and the first of Monet's paintings, which clearly implements many of the ideas of Impressionism. Painted in the seaside resort of Saint-Adresse, surrounded by members of the Monet family, it in no way demonstrates the internal contradictions in the artist's life. That summer, Camille Doncier, Claude's fiancee, was pregnant, and the artist's family strongly disapproved of the situation. His father issued an ultimatum regarding the financing of Monet's existence as an artist, saying that he should forget Camille and the child. In the summer, the younger Monet dutifully came to Saint-Adresse to calm his family, but did not know how to reconcile the desire of his relatives and his own decision to stay with Camille and his future child. In fact, this painting is an ode to sunny days on the Normandy coast, with an abundance of sailboats, umbrellas and flags, which testifies to the strictly ordered rest of the French bourgeoisie of that time. A gray-bearded gentleman in a panama hat in the foreground, modeled after Monet's father, Adolphe, sits with his back to the viewer, accompanied by a fashionably dressed woman in a white dress with an umbrella, also sitting on one of the bent reed chairs. It is generally believed that she represents Sophie Lecadre, the wife of Adolphe-Aime Lecadre. Those sitting watch the couple at the water's edge; she is wearing a fashionable white dress with red trim and an umbrella, and he is dressed more formally than Monet's father, in a top hat and a black jacket. The girl has been identified as Sophie's daughter, Jeanne-Marguerite, and the man, as usual, is believed to be her father and Sophie's husband, Dr. Adolphe-Aime Lecadre or another male relative. The four are surrounded by greenery, interspersed with flowers in red, yellow and white tones, where the latter echo the white dresses of the women. Gladioli, geraniums and nasturtiums, found in nature in a variety of shades of red, are combined here in one bright red tone. The French tricolor flies high on the right, while the red and yellow flag flies on the left, representing either the colors of the local sailing club or the colors of the Spanish flag in honor of Maria Cristina de Bourbon, widow of the Spanish King Ferdinand VII, who lived nearby at that time. Two people are sitting in a three-sail boat near a standing couple, other sailboats are moored on the left. Far out at sea, large ships can be seen, including steamships, which color the sky with gray smoke. Even in this relatively early work, the artist naturalistically depicted chimneys, just as he would later show factory chimneys behind the scenes of a bourgeois vacation in Argenteuil in the 1870s. The clear sky becomes more cloudy when it reaches the horizon line and the smoke from the ships. The long shadows cast by the figures in the foreground of the stage indicate that it is late afternoon.",
                descriptionRu = "За два года до написания картины \"Терраса в Сент-Адресс\" Моне писал: \"Я придумываю потрясающие вещи, когда еду в Сент-Адресс\". Действительно, настоящая работа - \"потрясающая вещь\" и первая из картин Моне, которая наглядно реализует многие идеи импрессионизма. Написанная в приморском курорте Сент-Адресс в окружении членов семьи Моне, она ни коим образом не демонстрирует внутренних противоречий в жизни художника. В то лето Камилла Донсье, невеста Клода, была беременна, и семья художника решительно не одобряла сложившуюся ситуацию. Его отец выдвинул ультиматум относительно финансирования существования Моне как художника, заявив, что тот должен забыть Камиллу и ребенка. Летом младший Моне покорно приехал в Сент-Адресс, чтобы успокоить свою семью, но не знал, как примирить желание родных, и собственное решение остаться с Камиллой и своим будущим ребенком. На самом деле, это полотно - ода солнечным дням на побережье Нормандии, с изобилием парусников, зонтиков и флагов, которая свидетельствует о строго упорядоченном отдыхе французской буржуазии того времени. Седобородый джентльмен в панаме на переднем плане, смоделированный с отца Моне, Адольфа, сидит спиной к зрителю в сопровождении модно одетой женщины в белом платье с зонтиком, также сидящей на одном из стульев из гнутого тростника. Принято считать, что она представляет Софи Лекадр, супругу Адольфа-Эме Лекадра. Сидящие наблюдают за парой у кромки воды; она в модном белом платье с красной отделкой и зонтиком, а он одет более официально, чем отец Моне, в цилиндре и черном пиджаке. Девушка была идентифицирована как дочь Софи, Жанна-Маргарита, а мужчина, как обычно, полагают, является ее отцом и мужем Софи, доктором Адольфом-Эме Лекадром или другим родственником мужского пола. Четверка окружена зеленью, перемежающейся цветами в красных, желтых и белых тонах, где последние вторят белым платьям женщин. Гладиолусы, герани и настурции, встречающиеся в природе в самых разных оттенках красного, объединены здесь в один ярко-красный тон. Французский триколор развевается высоко справа, в то время как слева реет красно-желтый флаг, представляющий либо цвета местного парусного клуба, либо цвета испанского флага в честь Марии Кристины де Бурбон, вдовы испанского короля Фердинанда VII, жившей тогда неподалеку. Два человека сидят в трехпарусной лодке возле стоящей пары, другие парусники пришвартованы слева. Далеко в море видны большие корабли, в том числе пароходы, которые окрашивают небо серым дымом. Даже в этой относительно ранней работе художник натуралистично изобразил дымовые трубы, так же как он позже покажет заводские трубы за кулисами буржуазного отдыха в Аржантей в 1870-х годах. Ясное небо становится более облачным, когда оно достигает линии горизонта и дыма от кораблей. Длинные тени, отбрасываемые фигурами на переднем плане сцены, выдают, что наступил поздний полдень.",

                didYouKnow = "In 1879, Monet exhibited a real painting at the fourth Impressionist exhibition called The Garden at Saint-Adresse. By 1915, she exhibited in San Francisco under the title \"Le Havre, terrace by the sea\" and at the New York World's Fair in 1940 as \"Harbor near Le Havre\". Both \"terrace\" and \"Le Havre\" were fixed in the name for a long time, until in 1860 at the Museum of Modern Art in New York \"Le Havre\" was replaced by the original \"Saint Address\". The return to the \"garden\" instead of \"terrace\" occurred in 1986 after studying the original catalog of the 1879 exhibition. Interestingly, in the Russian localization, the painting still bears the name \"Terrace in St. Address\", as the closest to the content of the canvas.",
                didYouKnowRu = "В 1879 году Моне выставил настоящую картину на четвертой выставке импрессионистов под названием \"Сад в Сент-Адресс\". К 1915 году она выставлялась в Сан-Франциско под заголовком \"Гавр, терраса у моря\" и на нью-йоркской Всемирной выставке 1940 года как \"Гавань возле Гавра\". И \"терраса\", и \"Гавр\" надолго закрепились в названии, пока в 1860 году в Музее современного искусства в Нью-Йорке \"Гавр\" не был заменен первоначальным \"Сент-Адресс\". Возвращение к \"саду\" вместо \"террасы\" произошло в 1986 году после изучения оригинального каталога выставки 1879 года. Интересно, что в русской локализации картина по прежнему носит название \"Терраса в Сент-Адресс\", как наиболее близкое к содержанию холста.",

                imageUrl = "https://impressionism.su/monet/picture/Garden%20at%20Sainte-Adresse.jpg",
                viewDate = "2023/12/01"
            ),
            ArtworkEntity(
                title = "Woman With A Parasol",
                titleRu = "Прогулка. Дама с зонтиком",

                creator = "Claude Monet",
                creatorRu = "Клод Моне",

                creationDate = "1875",
                creationDateRu = "1875",

                technique = "Oil painting",
                techniqueRu = "Масляная живопись",

                type = "Portrait",
                typeRu = "Портрет",

                description = "\"A walk. The Lady with the Umbrella\" is a painting painted by Claude Monet (Oscar-Claude Monet) in 1875, in Argenteuil, where the artist lived with his wife Camille and son Jean. The master created his masterpiece in the open air, while walking around the neighborhood. A summer day appears before the viewer. The figure of a woman is depicted from a lower angle and slightly shifted to the right. The author marked the diagonal with a shadow falling to the left and down. A gust of fabric folds and the slope of the grass create an amazing feeling of air swirling around an imaginary axis. Confident strokes, pure colors with a predominance of white and blue, give the impression of weightless lightness. It seems that now the muse of the painter will raise an umbrella and fly away after the clouds. A small figure of a child, depicted from the waist up, creates an additional feeling of free space near the main character.",
                descriptionRu = "«Прогулка. Дама с зонтиком» — картина, написанная Клодом Моне (Oscar-Claude Monet) в 1875 году, в Аржантёе, где художник проживал вместе с супругой Камиллой и сыном Жаном. Свой шедевр мастер создавал на пленэре, во время прогулки по окрестностям. Перед зрителем предстаёт летний день. Фигура женщины изображена с нижнего ракурса и чуть смещена вправо. Автор обозначил диагональ при помощи падающей влево и вниз тени. Порыв складок ткани и наклон травы создают удивительное ощущение завихрения воздуха вокруг воображаемой оси. Уверенные мазки, чистые краски с преобладанием белого и голубого, рождают впечатление невесомой лёгкости. Кажется, что сейчас муза живописца поднимет зонтик — и улетит вслед за облаками. Маленькая фигурка ребёнка, изображённая по пояс, создаёт дополнительное ощущение свободного пространства возле главной героини.",

                didYouKnow = "This painting reflects the essence of the impressionist look, as if stopping the bright moment that the master so wanted to preserve.",
                didYouKnowRu = "Картина «Прогулка. Дама с зонтиком» Клода Моне — самая известная из работ живописца. В ней отражена суть импрессионистского взгляда, словно останавливающего светлое мгновение, которое так хотел сохранить мастер.",

                imageUrl = "https://veryimportantlot.com/uploads/over/files/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2021/%D0%98%D1%8E%D0%BD%D1%8C%202021/%D0%A1%D1%82%D0%B0%D1%82%D1%8C%D1%8F%2018%20(1)%20%D0%9A%D0%BB%D0%BE%D0%B4%20%D0%9C%D0%BE%D0%BD%D0%B5.%20%D0%9A%D0%B0%D1%80%D1%82%D0%B8%D0%BD%D0%B0%20%C2%AB%D0%9F%D1%80%D0%BE%D0%B3%D1%83%D0%BB%D0%BA%D0%B0.%20%D0%94%D0%B0%D0%BC%D0%B0%20%D1%81%20%D0%B7%D0%BE%D0%BD%D1%82%D0%B8%D0%BA%D0%BE%D0%BC%C2%BB%2C%201875.jpg",
                viewDate = "2023/11/30"
            ),
            ArtworkEntity(
                title = "Grapevine",
                titleRu = "Виноградная лоза",

                creator = "Choe Seok-hwan",
                creatorRu = "Чхве Сок Хван",

                creationDate = "early 1800s",
                creationDateRu = "начало 1800-х",

                technique = "eight-panel folding screen; ink on paper",
                techniqueRu = "восьмипанельная складная ширма; бумага тушью",

                type = "Painting",
                typeRu = "Живопись",

                description = "July is the season of the ripening deep blue grapes, and also the month when the monsoon season begins in Korea. To the accompaniment of stormy summer wind, grape vines make a spectacular full circle across the surface of this eight-panel folding screen. The artist employed a variety of ink tones to create a sense of swift movement of grapevines amid turbulent storms. <br><br>Since their first introduction to the Korean peninsula around the 7th century through the Silk Road, grapes were used as artistic motifs. Artists embellished the surface of mother-of-pearl lacquer boxes or blue-and-white porcelain, while scholar-poets composed poems about the luscious sweet sourness of green grapes. By the late 19th century, grapes became the icon of fertility: the fruit grows in large clusters of many individual grapes, evoking the image of a stable clan with many descendants.",
                descriptionRu = "Июль - сезон созревания темно-синего винограда, а также месяц, когда в Корее начинается сезон муссонов. Под аккомпанемент штормового летнего ветра виноградные лозы описывают впечатляющий полный круг по поверхности этой восьмипанельной складной ширмы. Художник использовал различные оттенки туши, чтобы создать ощущение стремительного движения виноградных лоз среди бурных штормов. <br><br>С момента их первого появления на Корейском полуострове примерно в 7 веке по Шелковому пути виноград использовался в качестве художественного мотива. Художники украшали поверхность перламутровых лаковых шкатулок или бело-голубого фарфора, а ученые-поэты слагали стихи о сочной кисло-сладкой нотке зеленого винограда. К концу 19 века виноград стал символом плодородия: плоды растут большими гроздьями из множества отдельных сортов винограда, вызывая образ стабильного клана со множеством потомков.",

                didYouKnow = "Grapes in premodern East Asia symbolize many children.",
                didYouKnowRu = "Виноград в досовременной Восточной Азии символизировал множество детей.",


                imageUrl = "https://openaccess-cdn.clevelandart.org/2015.510/2015.510_web.jpg",
                viewDate = "2023/11/29"
            ),

            ArtworkEntity(
                title = "Stag at Sharkey's",
                titleRu = "Олень в Шарки",

                creator = "George Bellows",
                creatorRu = "Джордж Беллоуз",

                creationDate = "1909",
                creationDateRu = "1909",

                technique = "oil on canvas",
                techniqueRu = "масло на холсте",

                type = "Painting",
                typeRu = "Живопись",

                description = "Bellows was no stranger to Sharkey’s Athletic Club, a raucous saloon with a backroom boxing ring, located near his studio. Founded by Tom “Sailor” Sharkey, an ex-fighter who had also served in the US Navy, the club attracted men seeking to watch or participate in matches. Because public boxing was illegal in New York at the time, a private event had to be arranged in order for a bout to take place. Participation was usually limited to members of a particular club, but whenever an outsider competed, he was given temporary membership and known as a “stag.” Although boxing had its share of detractors who considered it uncouth at best or barbaric at worst, its proponents—among them President Theodore Roosevelt—regarded it a healthy manifestation of manliness. Around the time Bellows painted <em>Stag at Sharkey’s, </em>boxing was moving from a predominantly working-class enterprise to one with greater genteel appeal. For some contemporaries, boxing was a powerful analogy for the notion that only the strongest and fittest would flourish in modern society.",
                descriptionRu = "Беллоуз был не чужим для атлетического клуба Шарки, шумного салуна с боксерским рингом в задней комнате, расположенного недалеко от его студии. Клуб, основанный Томом «Моряком» Шарки, бывшим боксером, который также служил в ВМС США, привлекал мужчин, желающих наблюдать или участвовать в матчах. Поскольку публичный бокс был незаконен в Нью-Йорке в то время, для проведения боя необходимо было организовать частное мероприятие. Участие обычно ограничивалось членами определенного клуба, но когда участвовал посторонний, ему давали временное членство и называли «олень». Несмотря на то что у бокса было немало противников, которые считали его в лучшем случае невежественным или в худшем случае варварским, его сторонники, среди которых был президент Теодор Рузвельт, считали его здоровым проявлением мужественности. Примерно в то время, когда Беллоуз написал <em>Олень в Шарки</em>, бокс переходил из преимущественно рабочего класса к более джентльменскому. Для некоторых современников бокс был мощной аналогией для представления о том, что только самые сильные и подготовленные будут процветать в современном обществе.",

                didYouKnow = "George Bellows was raised in Columbus, Ohio, and attended Ohio State University where he played baseball and basketball.",
                didYouKnowRu = "Джордж Беллоуз вырос в Колумбусе, штат Огайо, и учился в Университете штата Огайо, где играл в бейсбол и баскетбол.",

                imageUrl = "https://openaccess-cdn.clevelandart.org/1922.1133/1922.1133_web.jpg",
                viewDate = "2023/11/28"
            ),

            ArtworkEntity(
                title = "Sir Anthony Mildmay, Knight of Apethorpe, Northamptonshire",
                titleRu = "Сэр Энтони Милдмэй, рыцарь из Апеторпа, Нортгемптоншир",

                creator = "Nicholas Hilliard",
                creatorRu = "Николас Хиллиард",

                creationDate = "c. 1590–93",
                creationDateRu = "ок. 1590–93",

                technique = "Watercolor on vellum, laid on card, mounted on wood",
                techniqueRu = "Акварель на пергаменте, прикрепленном к карточке, установленном на дереве",

                type = "Portrait Miniature",
                typeRu = "Миниатюрный портрет",

                description = "Framed by the folds of a circular tent, Sir Anthony Mildmay strikes a relaxed and confident pose, with his right hand resting on a cloth-covered table and his left grasping the hilt of his thin, double-edged sword called a rapier. Helmet, gauntlet, and leg coverings are scattered about, as if Mildmay has been interrupted in the act of putting on or taking off his armor. Despite its small scale, this portrait reveals Nicholas Hilliard’s emulation of (and rivalry with) life-size court portraiture, while his meticulous attention to detail and use of costly materials like shell gold reflect the refined world of courtly tournaments.",
                descriptionRu = "Окруженный складками круглой палатки, сэр Энтони Милдмэй занимает расслабленную и уверенную позу, его правая рука отдыхает на столе, покрытом тканью, а левая сжимает рукоять его тонкого двухлезвийного меча, называемого рапирой. Шлем, рукавица и накрытия для ног разбросаны вокруг, как будто Милдмэй был прерван в процессе надевания или снятия брони. Несмотря на свой малый размер, этот портрет показывает подражание Николаса Хиллиарда (и соперничество с) портретами в полный рост, в то время как его тщательное внимание к деталям и использование дорогих материалов, таких как золото в виде ракушки, отражают утонченный мир дворцовых турниров.",

                didYouKnow = "A little dog gazes adoringly at Mildmay who was by all accounts a very arrogant man.",
                didYouKnowRu = "Маленькая собака с восхищением смотрит на Милдмэя, который, по всем отчетам, был очень высокомерным человеком.",

                imageUrl = "https://openaccess-cdn.clevelandart.org/1926.554/1926.554_web.jpg",
                viewDate = "2023/11/27"
            ),

            ArtworkEntity(
                title = "Elizabeth Shewell West and Her Son, Raphael",
                titleRu = "Элизабет Шуэлл Уэст и ее сын Рафаэль",

                creator = "Benjamin West",
                creatorRu = "Бенджамин Уэст",

                creationDate = "c. 1770",
                creationDateRu = "ок. 1770 г.",

                technique = "oil on canvas",
                techniqueRu = "масло на холсте",

                type = "Painting",
                typeRu = "Живопись",

                description = "West was the first American artist to study in Italy, where he spent three years before permanently settling in London. He so admired the artistic ideals of the Italian Renaissance master Raphael that he named his eldest son after him, and he imitated Raphael’s celebrated Madonna of the Chair when composing this tender double portrait of his wife and child.",
                descriptionRu = "Уэст был первым американским художником, который учился в Италии, где он провел три года, прежде чем навсегда поселился в Лондоне. Он так восхищался художественными идеалами итальянского мастера эпохи Возрождения Рафаэля, что назвал своего старшего сына в его честь, и он подражал знаменитой Мадонне на стуле Рафаэля, когда составлял этот трогательный двойной портрет своей жены и ребенка.",

                didYouKnow = "Born in colonial America, West eventually became a court painter to George III of England.",
                didYouKnowRu = "Родившийся в колониальной Америке, Уэст в конечном итоге стал придворным художником Георга III Англии.",

                imageUrl = "https://openaccess-cdn.clevelandart.org/1927.393/1927.393_web.jpg",
                viewDate = "2023/11/26"
            ),

//            ArtworkEntity(
//                title = "",
//                titleRu = "",
//
//                creator = "",
//                creatorRu = "",
//
//                creationDate = "",
//                creationDateRu = "",
//
//                technique = "",
//                techniqueRu = "",
//
//                type = "",
//                typeRu = "",
//
//                description = "",
//                descriptionRu = "",
//
//                didYouKnow = "",
//                didYouKnowRu = "",
//
//                imageUrl = "",
//                viewDate = "26/11/2023"
//            ),
        )




        for (artwork in artworksList) {
            viewModel.createArtwork(artwork)
        }
    }

    override fun onArtworkClick(artworkId: Long) {
        val bottomSheetFragment = ArtworkBottomSheetFragment.newInstance(artworkId)
        bottomSheetFragment.show(supportFragmentManager, "ArtworkBottomSheetTag")
    }


}