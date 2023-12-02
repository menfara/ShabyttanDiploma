package farkhat.myrzabekov.shabyttan

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
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

//        createArtworks()

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
                viewDate = "29/11/2023"
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
                viewDate = "28/11/2023"
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
                viewDate = "27/11/2023"
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
                viewDate = "26/11/2023"
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