package farkhat.myrzabekov.shabyttan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.databinding.ActivityMainBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.OnArtworkClickListener
import farkhat.myrzabekov.shabyttan.presentation.ui.fragments.ArtworkBottomSheetFragment
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnArtworkClickListener {
    private val viewModel: MainViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { false }
        setContentView(binding.root)


//        viewModel.getArtworkByViewDate("29/11/2023")a

//        viewModel.todayArtworkLiveData.observe(this) { artwork ->
//            binding.textView.text = artwork?.titleRu ?: "Artwork not found"
//            binding.textView.text =
//                (binding.textView.text.toString() +"\n"+ artwork?.descriptionRu) ?: "Artwork not found"
//        }


//        createArtworks()



        handleDeepLink(intent)
        setupPreDrawListener()

        setupNavigation()
    }

    private fun setupPreDrawListener() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                private var isReady = false

                override fun onPreDraw(): Boolean {
                    if (!isReady) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            isReady = true
                            content.invalidate()
                        }, 400)
                        return false
                    } else {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        return true
                    }
                }
            }
        )
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
                    onArtworkClick(id)
                }
            }
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as? NavHostFragment
            ?: return
        navController = navHostFragment.navController




        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)


        binding.bottomnavigation.setupWithNavController(navController)
        graph.setStartDestination(R.id.homeFragment)


//        else {
//            binding.bottomnavigation.visibility = View.GONE
//            graph.setStartDestination(R.id.authorizationFragment3)
//        }

        navController.setGraph(graph, intent.extras)

    }



    private fun createArtworks() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isFirstLaunch = sharedPreferences.getBoolean("first_launch", true)
        Log.d(">>> isFirstLaunch", isFirstLaunch.toString())
        if (!isFirstLaunch) return
        with(sharedPreferences.edit()) {
            putBoolean("first_launch", false)
            apply()
        }
//        viewModel.createUser(
//            UserEntity(
//                username = "Farkhat",
//                email = "email@mail.com",
//                password = "pass"
//            )
//        )
//
//        viewModel.setUserId(1)
        viewModel.setUserLanguage()
        val artworksList = listOf(
            ArtworkEntity(
                title = "Sadko",
                titleRu = "Садко",

                creator = "Ilya Efimovich Repin",
                creatorRu = "Илья Ефимович Репин",

                creationDate = "1876",
                creationDateRu = "1876",

                technique = "Oil painting",
                techniqueRu = "Масляная живопись",

                type = "Literary scene",
                typeRu = "Литературная сцена",

                description = "Repin executed his Sadko painting in Paris. Having received the Great Gold Medal of the Academy of Arts for his Raising of Jairus’ Daughter, Repin received the right to a pensioner (that is, paid by the Academy) trip to Europe. The image of Sadko was painted from Victor Vasnetsov, who later became famous, among other things, for his fairy-tale subjects (he depicted his own Sadko). The theme of the picture was the Novgorod epic about the guslar Sadko, who became rich with the help of the sea king. His merchant ships froze in the middle of the sea and, to appease the sea lord, Sadko sank to the bottom. The painting depicts the moment when, by order of the sea king, he chooses a wife for himself in the underwater kingdom. Gorgeous beauties pass in front of him, but according to advice received from Nicholas of Mozhaysk (later called Nicholas the Wonderworker), Sadko chose a simple Russian girl Chernavka, who is visible in the background. To create the magical space of the underwater kingdom, Repin examined the Berlin marine aquarium, admired the marine life in Normandy, and visited the Crystal Palace in London. According to legend, he painted the seabed in the Parisian oceanarium. Bizarre algae, amazing fish, shellfish, starfish create a fabulous atmosphere. The chain of beauties passing in front of Sadko eradiates light, they seem to be underwater treasures worth descending to the bottom of the sea. Sadko is holding a gusli, his gaze is directed into the distance, where Chernavka is half-turned and also looks at him. In the same place, in the depths of space, a goldfish is visible — it was she who brought him good luck in the epic. One of the interpretations of the picture — the girls swimming in front of him personify different countries and artistic movements, but the hero’s heart is in a simple Russian girl. Such a version was suggested by Repin himself, speaking of the Sadko’s choice: “The idea expresses my present position and, perhaps, the position of all Russian art as yet.” It is appropriate to recall here that in 1874 the first exhibition of the Impressionists was held in Paris, and Repin visited it. He was fascinated by technique, but he found no sense in the new art movement. However, it’s not cut and dried; not accepting the “pure art”, Repin tried to follow its postulates from time to time, condemning the journalism of critical realism.",
                descriptionRu = "Репин выполнил свою картину \"Садко\" в Париже. Получив Большую золотую медаль Академии художеств за воспитание дочери Иаира, Репин получил право на пенсионную (то есть оплачиваемую Академией) поездку в Европу. Образ Садко был написан с Виктора Васнецова, который впоследствии прославился, помимо прочего, своими сказочными сюжетами (он изобразил своего собственного Садко). Темой картины стала новгородская былина о гусляре Садко, разбогатевшем с помощью морского царя. Его торговые корабли замерзли посреди моря, и, чтобы умилостивить морского владыку, Садко опустился на дно. На картине изображен момент, когда по приказу морского царя он выбирает себе жену в подводном царстве. Великолепные красавицы проходят перед ним, но по совету, полученному от Николая Можайского (позже названного Николаем Чудотворцем), Садко выбрал простую русскую девушку Чернавку, которая видна на заднем плане. Чтобы создать волшебное пространство подводного царства, Репин осмотрел Берлинский морской аквариум, полюбовался морскими обитателями Нормандии и посетил Хрустальный дворец в Лондоне. Согласно легенде, он нарисовал морское дно в парижском океанариуме. Причудливые водоросли, удивительные рыбы, моллюски, морские звезды создают сказочную атмосферу. Вереница красавиц, проходящих перед \"Садко\", затмевает свет, они кажутся подводными сокровищами, достойными того, чтобы опуститься на дно морское. Садко держит в руках гусли, его взгляд устремлен вдаль, где Чернавка стоит вполоборота и тоже смотрит на него. Там же, в глубинах космоса, видна золотая рыбка — именно она принесла ему удачу в эпопее. Одна из интерпретаций картины — проплывающие перед ним девушки олицетворяют разные страны и художественные течения, но сердце героя - в простой русской девушке. Такую версию предложил сам Репин, говоря о выборе “Садко”: \"Идея выражает мою нынешнюю позицию и, возможно, позицию всего русского искусства на данный момент\". Здесь уместно вспомнить, что в 1874 году в Париже состоялась первая выставка импрессионистов, и Репин посетил ее. Он был очарован техникой, но не нашел смысла в новом художественном движении. Однако это не так просто; не принимая “чистое искусство”, Репин время от времени пытался следовать его постулатам, осуждая публицистику критического реализма.",

                didYouKnow = "For the first time the public saw Sadko at the Paris Salon of 1876. Its appearance wasn’t noticed in France, but in Russia, Repin was awarded the title of academician for this work. The canvas was immediately acquired by the Grand Duke Alexander (future Emperor Alexander III). Meanwhile, the artist himself had a low opinion of the picture, considering it tasteless and vulgar. However, Repin was often overstrict with himself and admitted that seeing his paintings at exhibitions and in museums, he felt “hopelessly unhappy”, because he found his own creations mediocre.",
                didYouKnowRu = "Впервые публика увидела \"Садко\" на Парижском салоне 1876 года. Его появление не было замечено во Франции, но в России Репину за эту работу было присвоено звание академика. Полотно сразу же приобрел великий князь Александр (будущий император Александр III). Между тем сам художник был невысокого мнения о картине, считая ее безвкусной и вульгарной. Однако Репин часто был слишком строг к себе и признавался, что, видя свои картины на выставках и в музеях, чувствовал себя “безнадежно несчастным”, поскольку находил собственные творения посредственными.",

                imageUrl = "https://arthive.net/res/media/img/oy1200/work/659/294313@2x.webp",
                viewDate = "2023/12/05"
            ),

            ArtworkEntity(
                title = "The Death of Socrates",
                titleRu = "Смерть Сократа",

                creator = "Jacques-Louis David",
                creatorRu = "Жак Луи Давид",

                creationDate = "1787",
                creationDateRu = "1787",

                technique = "Oil painting",
                techniqueRu = "Масляная живопись",

                type = "Historical scene",
                typeRu = "Историческая сцена",

                description = "A bright representative of French classicism, Jacques-Louis David often chose scenes from ancient history for his canvases, which became close to him after his stay in Italy. In this painting, Socrates, ready to drink a cup of poison according to the verdict of the court, addresses the students with farewell words. The philosopher raised his hand and extended the other towards the bowl. His gesture, when his hand is about to touch a vessel with a deadly drink, but still does not touch it, hanging in the air, is key, because it creates the impression of stopped time. As a result, no matter how much the followers of Socrates suffered, death was defeated, because the teacher himself forgot about it, carried away by what he would tell his followers and leave behind. The theme of the immortality of the human spirit is emphasized by the greatness of the depicted people, expressed in their movements and faces, the restrained monochrome color of the canvas and the entire composition: the location of the characters along the front plane of the painting resembles a frieze, which gives solemnity to the whole scene. The artist-engraver John Boydell enthusiastically wrote to the English portrait painter Sir Joshua Reynolds that David's work is \"the greatest achievement in art after Michelangelo's Sistine Chapel and Raphael's Stanzas... This work would have done credit to Athens in the time of Pericles.\"",
                descriptionRu = "Яркий представитель французского классицизма Жак-Луи Давид нередко выбирал для своих полотен сцены из античной истории, ставшей ему близкой после пребывания в Италии. На этой картине Сократ, готовый выпить по приговору суда чашу с ядом, обращается к ученикам с прощальными словами. Философ поднял руку, а другую протянул в сторону чаши. Его жест, когда рука вот-вот коснется сосуда со смертельным напитком, но все-таки не касается его, повисая в воздухе, - ключевой, потому что создает впечатление остановившегося времени. В результате, как бы ни страдали последователи Сократа, смерть побеждена, потому что о ней забыл и сам учитель, увлеченный тем, что скажет своим последователям и оставит после себя. Тема бессмертия человеческого духа подчеркнута величием изображенных людей, выраженным в их движениях и лицах, сдержанным монохромным колоритом полотна и всей композицией: расположение персонажей вдоль передней плоскости картины напоминает фриз, что придает торжественность всей сцене. Художник-гравер Джон Бойдел восторженно писал английскому портретисту сэру Джошуа Рейнолдсу, что работа Давида - «величайшее достижение в искусстве после Сикстинской капеллы Микеланджело и Станц Рафаэля... Это произведение сделало бы честь Афинам во времена Перикла».",

                didYouKnow = "David conceived the idea of painting this canvas when the revolution in France was already close. The purpose of his work was to strengthen the spirit of fellow citizens by an example of Socratic fortitude.",
                didYouKnowRu = "Давид задумал написать это полотно, когда революция во Франции была уже близка. Целью его произведения было укрепление духа сограждан примером сократовской стойкости.",

                imageUrl = "https://arthive.net/res/media/img/oy800/work/f09/534774@2x.webp",
                viewDate = "2023/12/04"
            ),

            ArtworkEntity(
                title = "Cossacks writing a letter to the Turkish Sultan",
                titleRu = "Запорожцы",

                creator = "Ilya Efimovich Repin",
                creatorRu = "Илья Ефимович Репин",

                creationDate = "1891",
                creationDateRu = "1891",

                technique = "Oil painting",
                techniqueRu = "Масляная живопись",

                type = "Historical scene",
                typeRu = "Историческая сцена",

                description = "Repin's monumental painting \"The Cossacks\" depicts a historical incident inspired by an epistolary incident involving Cossacks and a Turkish bishop. The artist gathered material for the painting during his visits to Ukraine in the 1880s, where he was accompanied by his apprentice Valentin Serov. The painting captures the irrepressible spirit of freedom among the Ukrainian Cossacks, influenced in part by Gogol's \"Taras Bulba.\" The composition features prominent figures such as Taras Bulba and his son Andrew, along with chieftain Ivan Sirko and advisor Dmitry Yavorsky. Repin carefully orchestrated the circular composition, symbolizing the power and strength of Zaporozhye freemen. The painting serves as an encyclopedia of laughter, depicting various shades of humor, from a thin smile to infectious laughter. Repin faced criticism, especially when he \"closed\" one of the Cossack figures, altering the original composition. Despite doubts about his talent, Repin remained unwavering in his vision for \"The Cossacks,\" considering it a harmonious masterpiece that captures the essence of the rebellious, freedom-loving Cossacks and the spirit of the Zaporozhian Sich.",
                descriptionRu = "Монументальная картина Репина \"Казаки\" изображает историческое происшествие, вдохновленное эпистолярным инцидентом с участием казаков и турецкого епископа. Художник собрал материал для картины во время своих визитов в Украину в 1880-х годах, где его сопровождал его ученик Валентин Серов. Картина отражает неуемный дух свободы среди украинского казачества, отчасти под влиянием гоголевского \"Тараса Бульбы\". В композиции представлены такие выдающиеся фигуры, как Тарас Бульба и его сын Андрей, а также атаман Иван Сирко и советник Дмитрий Яворский. Репин тщательно срежиссировал круговую композицию, символизирующую мощь и крепкую силу запорожской вольницы. Картина служит энциклопедией смеха, изображая различные оттенки юмора, от тонкой улыбки до заразительного смеха. Репин столкнулся с критикой, особенно когда он \"закрыл\" одну из фигур казака, изменив первоначальную композицию. Несмотря на сомнения в своем таланте, Репин остался непоколебим в своем видении \"Казаков\", считая его гармоничным шедевром, передающим сущность мятежного, свободолюбивого казачества и дух Запорожской Сечи.",

                didYouKnow = "Despite being a renowned artist, Repin had a playful and humorous side. He was known for creating caricatures and cartoons, often entertaining his friends and fellow artists with his wit. This lighter side of Repin, expressed through humor and satire, provided a delightful contrast to the powerful and serious themes often depicted in his major works. It showcases that even great artists have a capacity for joy and levity in addition to their profound artistic skills.",
                didYouKnowRu = "Несмотря на то, что Репин был известным художником, у него была игривая и юмористическая сторона. Он был известен тем, что создавал карикатуры и карикатуристов, часто развлекая своих друзей и коллег-художников своим остроумием. Эта светлая сторона Репина, выраженная через юмор и сатиру, создавала восхитительный контраст с сильными и серьезными темами, часто изображаемыми в его основных работах. Это демонстрирует, что даже великие художники обладают способностью к радости и легкомыслию в дополнение к своим глубоким художественным навыкам.",

                imageUrl = "https://arthive.net/res/media/img/oy800/work/e28/352452@2x.webp",
                viewDate = "2023/12/03"
            ),

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

    override fun onArtworkClick(artworkId: String) {
        val bottomSheetFragment = ArtworkBottomSheetFragment.newInstance(artworkId)
        bottomSheetFragment.show(supportFragmentManager, "ArtworkBottomSheetTag")
    }


}