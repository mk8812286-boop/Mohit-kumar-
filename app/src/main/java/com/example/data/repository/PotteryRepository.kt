package com.example.data.repository

import com.example.R
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class PotteryRepository {

    private val _currentRole = MutableStateFlow(UserRole.CUSTOMER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _language = MutableStateFlow(AppLanguage.HINDI)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _selectedDistanceKm = MutableStateFlow(10) // 2, 5, 10, 20
    val selectedDistanceKm: StateFlow<Int> = _selectedDistanceKm.asStateFlow()

    private val _selectedCategory = MutableStateFlow(PotteryCategory.ALL)
    val selectedCategory: StateFlow<PotteryCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _customerLocation = MutableStateFlow("जयपुर (Jaipur), राजस्थान - 302001")
    val customerLocation: StateFlow<String> = _customerLocation.asStateFlow()

    private val _adminCommissionPercent = MutableStateFlow(10.0)
    val adminCommissionPercent: StateFlow<Double> = _adminCommissionPercent.asStateFlow()

    // Pre-populated Shops
    private val initialShops = listOf(
        PotteryShop(
            id = "shop_1",
            nameHi = "रामेश्वर प्रजापति पॉटरी वर्क्स",
            nameEn = "Rameshwar Prajapati Pottery Works",
            potterNameHi = "रामेश्वर प्रजापति (मास्टर शिल्पी)",
            potterNameEn = "Rameshwar Prajapati (Master Artisan)",
            phone = "+91 98290 44551",
            villageCity = "कुम्हार मोहल्ला, सांगानेर",
            districtState = "जयपुर, राजस्थान",
            fullAddress = "प्लॉट 12, कुम्हार गली, सांगानेर मंडी रोड, जयपुर",
            pinCode = "302029",
            distanceKm = 2.4,
            rating = 4.9,
            reviewCount = 142,
            openingHours = "सुबह 7:00 - रात 8:30",
            upiId = "rameshwar.pottery@okhdfcbank",
            bankAccount = "SBI A/C: 40912849102",
            isApproved = true,
            specialtyHi = "शुद्ध प्राकृतिक जल मटके व पारंपरिक कुल्हड़",
            specialtyEn = "Pure natural cooling matkas & authentic kulhads",
            yearsExperience = 32
        ),
        PotteryShop(
            id = "shop_2",
            nameHi = "माटी क्रिएशन्स - मोहन लाल कुम्हार",
            nameEn = "Maati Creations - Mohan Lal",
            potterNameHi = "मोहन लाल प्रजापति",
            potterNameEn = "Mohan Lal Prajapati",
            phone = "+91 97840 99882",
            villageCity = "आमेर रोड, पुराना शहर",
            districtState = "जयपुर, राजस्थान",
            fullAddress = "दुकान 4, हस्तशिल्प बाजार, आमेर, जयपुर",
            pinCode = "302002",
            distanceKm = 4.8,
            rating = 4.8,
            reviewCount = 98,
            openingHours = "सुबह 8:00 - रात 9:00",
            upiId = "maati.mohan@okaxis",
            bankAccount = "BOB A/C: 98127391823",
            isApproved = true,
            specialtyHi = "हांडी, तवा व आधुनिक टेराकोटा डिनरवेयर",
            specialtyEn = "Clay handis, tawas & artisanal dining sets",
            yearsExperience = 24
        ),
        PotteryShop(
            id = "shop_3",
            nameHi = "कलाकुंभ टेराकोटा स्टूडियो",
            nameEn = "KalaKumbh Terracotta Studio",
            potterNameHi = "कैलाश चन्द कुम्हार व परिवार",
            potterNameEn = "Kailash Chand & Family",
            phone = "+91 94140 33214",
            villageCity = "बगरू हस्तकला ग्राम",
            districtState = "जयपुर, राजस्थान",
            fullAddress = "शिल्पग्राम चौक, बगरू, जयपुर",
            pinCode = "303007",
            distanceKm = 9.1,
            rating = 4.7,
            reviewCount = 76,
            openingHours = "सुबह 9:00 - शाम 7:30",
            upiId = "kalakumbh@upi",
            bankAccount = "PNB A/C: 11209384912",
            isApproved = true,
            specialtyHi = "सजावटी फूलदान, उरली व पारंपरिक दीये",
            specialtyEn = "Decorative vases, floral urlis & festive diyas",
            yearsExperience = 28
        ),
        PotteryShop(
            id = "shop_4",
            nameHi = "श्री गणेश मिट्टी बर्तन भंडार",
            nameEn = "Shri Ganesh Mitti Bhandar",
            potterNameHi = "गोपाल प्रजापति",
            potterNameEn = "Gopal Prajapati",
            phone = "+91 98285 77612",
            villageCity = "चाकसू मोड़",
            districtState = "जयपुर, राजस्थान",
            fullAddress = "मेन हाईवे मार्केट, चाकसू, जयपुर",
            pinCode = "303901",
            distanceKm = 17.5,
            rating = 4.6,
            reviewCount = 52,
            openingHours = "सुबह 6:30 - रात 8:00",
            upiId = "gopal.clay@paytm",
            bankAccount = "HDFC A/C: 50100982736",
            isApproved = true,
            specialtyHi = "सुराही, पूजा के बर्तन व कस्टम मिट्टी के बर्तन",
            specialtyEn = "Surahis, ritual pottery & custom artisan work",
            yearsExperience = 19
        )
    )

    private val _shops = MutableStateFlow(initialShops)
    val shops: StateFlow<List<PotteryShop>> = _shops.asStateFlow()

    // Sizes
    private val standardSizes = listOf(
        ProductVariantSize("छोटा (Small)", "Small", "2.5 L", "18 x 18 cm", "1.2 kg", 0.0),
        ProductVariantSize("मध्यम (Medium)", "Medium", "5.0 L", "24 x 24 cm", "2.1 kg", 80.0),
        ProductVariantSize("बड़ा (Large)", "Large", "8.0 L", "30 x 30 cm", "3.4 kg", 160.0)
    )

    private val kulhadSizes = listOf(
        ProductVariantSize("12 कुल्हड़ सेट", "Pack of 12", "150 ml प्रत्येक", "7 x 7 cm", "1.5 kg", 0.0),
        ProductVariantSize("24 कुल्हड़ सेट", "Pack of 24", "150 ml प्रत्येक", "7 x 7 cm", "3.0 kg", 90.0),
        ProductVariantSize("50 कुल्हड़ पार्टी पैक", "Pack of 50", "150 ml प्रत्येक", "7 x 7 cm", "6.0 kg", 240.0)
    )

    private val handiSizes = listOf(
        ProductVariantSize("1.5 लीटर (1-2 सदस्य)", "1.5 Litre (Small)", "1.5 L", "16 x 14 cm", "1.1 kg", 0.0),
        ProductVariantSize("3.0 लीटर (3-5 सदस्य)", "3.0 Litre (Medium)", "3.0 L", "22 x 18 cm", "2.2 kg", 120.0),
        ProductVariantSize("5.0 लीटर दावत साइज", "5.0 Litre (Large)", "5.0 L", "28 x 22 cm", "3.5 kg", 240.0)
    )

    private val colorsList = listOf(
        ClayColor("प्राकृतिक लाल गेरू (Natural Terracotta)", "Terracotta Red", 0xFFA65E3B),
        ClayColor("पारंपरिक काली मिट्टी (Smoked Black Clay)", "Smoked Black", 0xFF352C27),
        ClayColor("गोल्डन बॉर्डर गेरुआ (Antique Gold Accent)", "Gold Trim Clay", 0xFFC49A59),
        ClayColor("धूप पकी पीली मिट्टी (Sun-baked Ochre)", "Ochre Clay", 0xFFD89B5B)
    )

    private val initialProducts = listOf(
        PotteryProduct(
            id = "prod_1",
            nameHi = "प्राकृतिक शीतल जल मटका (टोंटी सहित)",
            nameEn = "Natural Cooling Water Matka (With Brass Tap)",
            descriptionHi = "बिना किसी केमिकल या पॉलिश के शुद्ध प्राकृतिक मिट्टी से चाक पर हस्तनिर्मित मटका। इसकी प्राकृतिक छिद्रयुक्त मिट्टी पानी को फ्रिज से भी अधिक स्वास्थ्यवर्धक और प्राकृतिक रूप से ठंडा रखती है। नल (Tap) फिटेड होने से उपयोग बेहद आसान है।",
            descriptionEn = "Authentic handcrafted earthen water pot with food-grade brass tap. Naturally porous clay keeps drinking water refreshingly cool without electricity, infused with natural alkaline minerals.",
            category = PotteryCategory.MATKA,
            basePrice = 380.0,
            originalPrice = 499.0,
            shopId = "shop_1",
            shopNameHi = "रामेश्वर प्रजापति पॉटरी वर्क्स",
            shopNameEn = "Rameshwar Prajapati Pottery Works",
            potterNameHi = "रामेश्वर प्रजापति",
            potterNameEn = "Rameshwar Prajapati",
            rating = 4.9,
            reviewCount = 88,
            sizes = standardSizes,
            colors = colorsList.take(3),
            finishType = "छिद्रयुक्त प्राकृतिक मटका (Unglazed Breathable)",
            careInstructionsHi = "प्रथम उपयोग से पहले मटके को 12 घंटे साफ ठंडे पानी में डुबो कर रखें। साबुन या डिटर्जेंट का प्रयोग कभी न करें।",
            careInstructionsEn = "Soak in clean water for 12 hours before initial use. Rinse only with warm water; never use chemical dish soap.",
            stockQuantity = 24,
            imageDrawableRes = R.drawable.ic_pottery_logo
        ),
        PotteryProduct(
            id = "prod_2",
            nameHi = "देसी मिट्टी के कुल्हड़ (12 का सेट)",
            nameEn = "Pure Earth Chai Kulhad Set (Pack of 12)",
            descriptionHi = "चाय के शौकीनों के लिए असली सोंधी खुशबू वाले मिट्टी के कुल्हड़। धीमी आंच पर पकाई गई शुद्ध मिट्टी, जो चाय का स्वाद कई गुना बढ़ा देती है। 100% बायोडिग्रेडेबल और पर्यावरण अनुकूल।",
            descriptionEn = "Set of 12 traditional baked clay cups offering genuine earthy aroma for tea and lassi. Handcrafted, unglazed and 100% eco-friendly.",
            category = PotteryCategory.KULHAD,
            basePrice = 140.0,
            originalPrice = 199.0,
            shopId = "shop_1",
            shopNameHi = "रामेश्वर प्रजापति पॉटरी वर्क्स",
            shopNameEn = "Rameshwar Prajapati Pottery Works",
            potterNameHi = "रामेश्वर प्रजापति",
            potterNameEn = "Rameshwar Prajapati",
            rating = 4.8,
            reviewCount = 120,
            sizes = kulhadSizes,
            colors = listOf(colorsList[0], colorsList[1]),
            finishType = "पारंपरिक कच्चा पक्का (Matte Clay)",
            careInstructionsHi = "सीधे गर्म चाय या दूध परोसें। पुन: उपयोग के लिए केवल गुनगुने पानी से धोएं।",
            careInstructionsEn = "Ready for serving hot tea. Gently rinse with warm water for reuse.",
            stockQuantity = 80,
            imageDrawableRes = R.drawable.ic_pottery_logo
        ),
        PotteryProduct(
            id = "prod_3",
            nameHi = "दम बिरयानी व दाल मिट्टी की हांडी (ढक्कन सहित)",
            nameEn = "Slow-Cook Biryani & Dal Clay Handi with Lid",
            descriptionHi = "पारंपरिक धीमी आंच पर पौष्टिक भोजन पकाने के लिए विशेष मोटी मिट्टी की हांडी। इसमें पकने वाले भोजन के 100% पोषक तत्व और प्राकृतिक स्वाद सुरक्षित रहते हैं। गैस चूल्हे व अंगीठी दोनों पर उपयुक्त।",
            descriptionEn = "Heavy clay cooking handi with snug clay lid for slow-cooked curries, dals, and fragrant dum biryani. Retains full nutritional value.",
            category = PotteryCategory.HANDI,
            basePrice = 450.0,
            originalPrice = 599.0,
            shopId = "shop_2",
            shopNameHi = "माटी क्रिएशन्स - मोहन लाल",
            shopNameEn = "Maati Creations - Mohan Lal",
            potterNameHi = "मोहन लाल प्रजापति",
            potterNameEn = "Mohan Lal Prajapati",
            rating = 4.9,
            reviewCount = 64,
            sizes = handiSizes,
            colors = listOf(colorsList[0], colorsList[2]),
            finishType = "ऑर्गेनिक सरसों तेल से सीजन की हुई (Pre-seasoned with Mustard Oil)",
            careInstructionsHi = "हांडी को पहले उपयोग से पहले 6 घंटे चावल के मांड (पानी) में रखें। हमेशा धीमी से मध्यम आंच पर ही पकाएं।",
            careInstructionsEn = "Season with rice water before first use. Always cook on low-to-medium flame.",
            stockQuantity = 18,
            imageDrawableRes = R.drawable.ic_pottery_logo
        ),
        PotteryProduct(
            id = "prod_4",
            nameHi = "शाही मिट्टी की सुराही (नक्काशीदार)",
            nameEn = "Royal Carved Terracotta Water Surahi",
            descriptionHi = "राजस्थानी पारंपरिक नक्काशी वाली मिट्टी की लंबी गर्दन वाली सुराही। यह न केवल पानी को प्राकृतिक रूप से ठंडा और मीठा बनाती है, बल्कि आपके डाइनिंग टेबल की शोभा भी बढ़ाती है।",
            descriptionEn = "Intricately hand-engraved long-necked clay flask. Cools water through gentle evaporation while adding timeless royal Indian charm.",
            category = PotteryCategory.SURAHI,
            basePrice = 320.0,
            originalPrice = 420.0,
            shopId = "shop_4",
            shopNameHi = "श्री गणेश मिट्टी बर्तन भंडार",
            shopNameEn = "Shri Ganesh Mitti Bhandar",
            potterNameHi = "गोपाल प्रजापति",
            potterNameEn = "Gopal Prajapati",
            rating = 4.7,
            reviewCount = 41,
            sizes = standardSizes.take(2),
            colors = listOf(colorsList[0], colorsList[2]),
            finishType = "गोल्डन बॉर्डर नक्काशी (Gold Carved)",
            careInstructionsHi = "अंदर से ब्रश से हल्के हाथ से साफ करें। बाहर की नक्काशी को सूखे कपड़े से पोंछें।",
            careInstructionsEn = "Clean inside gently with soft brush; wipe external artwork with dry cloth.",
            stockQuantity = 15,
            imageDrawableRes = R.drawable.ic_pottery_logo
        ),
        PotteryProduct(
            id = "prod_5",
            nameHi = "शुद्ध मिट्टी का रोटी तवा (हैंडल सहित)",
            nameEn = "Pure Organic Clay Roti Tawa (With Wooden Handle)",
            descriptionHi = "बिना टेफ्लॉन या नॉन-स्टिक कोटिंग का 100% प्राकृतिक मिट्टी का तवा। इसमें सिकी रोटियां अधिक फूली हुई, मुलायम और पाचन के लिए अत्यंत गुणकारी होती हैं।",
            descriptionEn = "100% non-toxic terracotta roti tawa. Clay baking ensures softer, tastier rotis that preserve wheat nutrients and natural carbohydrates.",
            category = PotteryCategory.TAWA,
            basePrice = 280.0,
            originalPrice = 360.0,
            shopId = "shop_2",
            shopNameHi = "माटी क्रिएशन्स - मोहन लाल",
            shopNameEn = "Maati Creations - Mohan Lal",
            potterNameHi = "मोहन लाल प्रजापति",
            potterNameEn = "Mohan Lal Prajapati",
            rating = 4.8,
            reviewCount = 115,
            sizes = listOf(
                ProductVariantSize("9 इंच (सामान्य)", "9 Inch Regular", "1 पीस", "23 cm व्यास", "950 gm", 0.0),
                ProductVariantSize("11 इंच (बड़ा)", "11 Inch Large", "1 पीस", "28 cm व्यास", "1.4 kg", 60.0)
            ),
            colors = listOf(colorsList[0], colorsList[1]),
            finishType = "स्मूद बेक्ड क्ले (Smooth Baked)",
            careInstructionsHi = "तवे को सीधे तेज आंच पर न रखें। गर्म तवे पर ठंडा पानी न डालें।",
            careInstructionsEn = "Heat gradually on low flame. Do not pour cold water directly onto hot tawa.",
            stockQuantity = 35,
            imageDrawableRes = R.drawable.ic_pottery_logo
        ),
        PotteryProduct(
            id = "prod_6",
            nameHi = "हस्तनिर्मित उरली व दीया सेट (21 दीये)",
            nameEn = "Handcrafted Earthen Urli & Festive Diya Set (21 Pcs)",
            descriptionHi = "शुभ अवसरों, पूजा और गृह सजावट हेतु विशेष लाल गेरुई मिट्टी के दीये व फूलों से सजाने वाली फ्लोटिंग उरली। पारंपरिक शुभ कलाकृतियों से सज्जित।",
            descriptionEn = "Festive celebration clay set featuring a decorative floral water urli and 21 handmade clay oil lamps for sacred pooja and festival glow.",
            category = PotteryCategory.DIYA,
            basePrice = 220.0,
            originalPrice = 299.0,
            shopId = "shop_3",
            shopNameHi = "कलाकुंभ टेराकोटा स्टूडियो",
            shopNameEn = "KalaKumbh Terracotta Studio",
            potterNameHi = "कैलाश चन्द कुम्हार",
            potterNameEn = "Kailash Chand",
            rating = 4.9,
            reviewCount = 92,
            sizes = listOf(
                ProductVariantSize("स्टैंडर्ड पैक (21 दीये + 1 उरली)", "Standard Festive Set", "22 बर्तन", "26 cm उरली", "1.8 kg", 0.0),
                ProductVariantSize("बड़ा पूजा सेट (51 दीये + 2 उरली)", "Grand Puja Set", "53 बर्तन", "32 cm उरली", "3.6 kg", 180.0)
            ),
            colors = listOf(colorsList[2], colorsList[0]),
            finishType = "गोल्डन बॉर्डर व प्राकृतिक गेरू (Gold & Ochre)",
            careInstructionsHi = "तेल बत्ती के बाद हल्के गर्म पानी से धोकर सुखा लें।",
            careInstructionsEn = "Clean gently with warm water after oil use and dry in sunlight.",
            stockQuantity = 50,
            imageDrawableRes = R.drawable.ic_pottery_logo
        ),
        PotteryProduct(
            id = "prod_7",
            nameHi = "पारंपरिक हस्तनिर्मित टेराकोटा गमला (12 इंच)",
            nameEn = "Handmade Terracotta Plant Pot (12 Inch)",
            descriptionHi = "पौधों की जड़ों में हवा और नमी बनाए रखने वाला शुद्ध मिट्टी का सांस लेने वाला गमला। प्लास्टिक के गमलों से कहीं बेहतर, पौधों को गर्मी में भी ठंडा रखता है।",
            descriptionEn = "Breathable terracotta planter with drainage hole. Natural porosity keeps plant roots oxygenated and prevents root rot.",
            category = PotteryCategory.GAMLA,
            basePrice = 190.0,
            originalPrice = 250.0,
            shopId = "shop_3",
            shopNameHi = "कलाकुंभ टेराकोटा स्टूडियो",
            shopNameEn = "KalaKumbh Terracotta Studio",
            potterNameHi = "कैलाश चन्द कुम्हार",
            potterNameEn = "Kailash Chand",
            rating = 4.6,
            reviewCount = 37,
            sizes = listOf(
                ProductVariantSize("8 इंच गमला", "8 Inch Pot", "4 L मिट्टी", "20 x 20 cm", "2.1 kg", 0.0),
                ProductVariantSize("12 इंच गमला", "12 Inch Pot", "9 L मिट्टी", "30 x 30 cm", "4.2 kg", 90.0),
                ProductVariantSize("16 इंच विशाल गमला", "16 Inch Large Pot", "18 L मिट्टी", "40 x 40 cm", "7.5 kg", 210.0)
            ),
            colors = listOf(colorsList[0], colorsList[3]),
            finishType = "पारंपरिक उभरी डिजाइन (Embossed Floral)",
            careInstructionsHi = "नीचे जल निकासी छेद में कंकड़ रखें और प्राकृतिक खाद वाली मिट्टी भरें।",
            careInstructionsEn = "Place pebbles over bottom drainage hole before adding rich potting soil.",
            stockQuantity = 40,
            imageDrawableRes = R.drawable.ic_pottery_logo
        ),
        PotteryProduct(
            id = "prod_8",
            nameHi = "मिट्टी की पारंपरिक डिनर थाली व कटोरी सेट",
            nameEn = "Royal Clay Dinner Thali & 4 Bowls Set",
            descriptionHi = "शाही अंदाज में भोजन परोसने के लिए मिट्टी की सुंदर थाली, 4 कटोरियां, 1 चम्मच और 1 लस्सी का गिलास। स्वास्थ्य के लिए अत्यंत लाभकारी।",
            descriptionEn = "Full traditional clay dining set including 1 large platter thali, 4 curio bowls, spoon and tumbler for wholesome cultural dining.",
            category = PotteryCategory.PLATE_BOWL,
            basePrice = 360.0,
            originalPrice = 480.0,
            shopId = "shop_2",
            shopNameHi = "माटी क्रिएशन्स - मोहन लाल",
            shopNameEn = "Maati Creations - Mohan Lal",
            potterNameHi = "मोहन लाल प्रजापति",
            potterNameEn = "Mohan Lal Prajapati",
            rating = 4.8,
            reviewCount = 59,
            sizes = listOf(
                ProductVariantSize("1 व्यक्ति डिनर सेट", "Single Dining Set", "6 बर्तन", "32 cm थाली", "2.3 kg", 0.0),
                ProductVariantSize("2 व्यक्ति कपल सेट", "Couple Dining Set", "12 बर्तन", "32 cm x 2 थाली", "4.6 kg", 320.0)
            ),
            colors = listOf(colorsList[0], colorsList[1]),
            finishType = "स्मूद फूड-ग्रेड फिनिश (Smooth Food-Grade)",
            careInstructionsHi = "भोजन के तुरंत बाद धोएं। खुरदरे स्क्रबर का प्रयोग न करें।",
            careInstructionsEn = "Wash immediately after meals with soft sponge. Do not use metal scrubbers.",
            stockQuantity = 22,
            imageDrawableRes = R.drawable.ic_pottery_logo
        ),
        PotteryProduct(
            id = "prod_9",
            nameHi = "हस्तनिर्मित सजावटी मिट्टी का फूलदान",
            nameEn = "Handcrafted Antique Terracotta Vase",
            descriptionHi = "दीवार व टेबल सजावट हेतु प्राचीन भारतीय शैली में बना खूबसूरत टेराकोटा फूलदान। बारीक रेखांकन व गोल्डन बॉर्डर से अलंकृत।",
            descriptionEn = "Artisanal terracotta floor and tabletop vase featuring exquisite relief patterns and warm rustic finish.",
            category = PotteryCategory.DECORATIVE,
            basePrice = 420.0,
            originalPrice = 550.0,
            shopId = "shop_3",
            shopNameHi = "कलाकुंभ टेराकोटा स्टूडियो",
            shopNameEn = "KalaKumbh Terracotta Studio",
            potterNameHi = "कैलाश चन्द कुम्हार",
            potterNameEn = "Kailash Chand",
            rating = 4.7,
            reviewCount = 29,
            sizes = standardSizes.take(2),
            colors = listOf(colorsList[2], colorsList[0]),
            finishType = "एंटीक गोल्ड रिलीफ (Antique Gold Finish)",
            careInstructionsHi = "केवल सूखे या हल्के नम सूती कपड़े से साफ करें।",
            careInstructionsEn = "Wipe clean with a soft dry or slightly damp cotton cloth.",
            stockQuantity = 14,
            imageDrawableRes = R.drawable.ic_pottery_logo
        ),
        PotteryProduct(
            id = "prod_10",
            nameHi = "पवित्र पूजा हवन कुण्ड व कलश",
            nameEn = "Sacred Pooja Earthen Kalash & Hawan Kund",
            descriptionHi = "धार्मिक अनुष्ठानों, गृह प्रवेश व नवरात्रि पूजा के लिए शुद्ध वैदिक नियमों के अनुरूप तैयार मिट्टी का कलश और हवन कुण्ड।",
            descriptionEn = "Purified sacred ritual earthen kalash with coconut seat and durable hawan kund handcrafted for Indian spiritual ceremonies.",
            category = PotteryCategory.PUJA,
            basePrice = 250.0,
            originalPrice = 320.0,
            shopId = "shop_4",
            shopNameHi = "श्री गणेश मिट्टी बर्तन भंडार",
            shopNameEn = "Shri Ganesh Mitti Bhandar",
            potterNameHi = "गोपाल प्रजापति",
            potterNameEn = "Gopal Prajapati",
            rating = 4.9,
            reviewCount = 47,
            sizes = standardSizes.take(2),
            colors = listOf(colorsList[0], colorsList[2]),
            finishType = "वैदिक प्राकृतिक गेरुआ (Vedic Natural Red)",
            careInstructionsHi = "पूजा उपरांत पवित्र जल से धोकर शुद्ध स्थान पर रखें।",
            careInstructionsEn = "Rinse with clean water after rituals and store in sacred place.",
            stockQuantity = 28,
            imageDrawableRes = R.drawable.ic_pottery_logo
        )
    )

    private val _products = MutableStateFlow(initialProducts)
    val products: StateFlow<List<PotteryProduct>> = _products.asStateFlow()

    // Shopping Cart
    private val _cartItems = MutableStateFlow<List<OrderItem>>(emptyList())
    val cartItems: StateFlow<List<OrderItem>> = _cartItems.asStateFlow()

    // Wishlist
    private val _wishlistIds = MutableStateFlow<Set<String>>(setOf("prod_1", "prod_3"))
    val wishlistIds: StateFlow<Set<String>> = _wishlistIds.asStateFlow()

    // Favorite Shops
    private val _favoriteShopIds = MutableStateFlow<Set<String>>(setOf("shop_1"))
    val favoriteShopIds: StateFlow<Set<String>> = _favoriteShopIds.asStateFlow()

    // Initial Orders
    private val initialOrders = listOf(
        Order(
            id = "ord_101",
            orderNumber = "PK-9824",
            customerName = "राजेश शर्मा (Rajesh Sharma)",
            customerPhone = "+91 98291 00221",
            deliveryAddress = "मकान नं. 45, वैशाली नगर, जयपुर - 302021",
            items = listOf(
                OrderItem(
                    product = initialProducts[0],
                    selectedSize = initialProducts[0].sizes[1],
                    selectedColor = initialProducts[0].colors[0],
                    quantity = 1,
                    unitPrice = 460.0
                )
            ),
            subtotal = 460.0,
            deliveryFee = 40.0,
            commissionPercent = 10.0,
            commissionAmount = 46.0,
            sellerPayout = 414.0,
            totalAmount = 500.0,
            paymentMethod = PaymentMethod.UPI,
            isPaid = true,
            status = OrderStatus.OUT_FOR_DELIVERY,
            orderTimestamp = System.currentTimeMillis() - 45 * 60 * 1000,
            deliveryBoyName = "सुरेश कुमार (Suresh Kumar)",
            deliveryBoyPhone = "+91 98290 12345",
            pickupOtp = "4812",
            deliveryOtp = "7390",
            packingPhotoTaken = true,
            liveDistanceRemainingKm = 1.4,
            liveEstimatedMinutes = 8
        ),
        Order(
            id = "ord_102",
            orderNumber = "PK-9780",
            customerName = "अनीता वर्मा (Anita Verma)",
            customerPhone = "+91 94140 88771",
            deliveryAddress = "फ्लैट 302, मानसरोवर, जयपुर - 302020",
            items = listOf(
                OrderItem(
                    product = initialProducts[1],
                    selectedSize = initialProducts[1].sizes[0],
                    selectedColor = initialProducts[1].colors[0],
                    quantity = 2,
                    unitPrice = 140.0
                ),
                OrderItem(
                    product = initialProducts[4],
                    selectedSize = initialProducts[4].sizes[0],
                    selectedColor = initialProducts[4].colors[0],
                    quantity = 1,
                    unitPrice = 280.0
                )
            ),
            subtotal = 560.0,
            deliveryFee = 40.0,
            commissionPercent = 10.0,
            commissionAmount = 56.0,
            sellerPayout = 504.0,
            totalAmount = 600.0,
            paymentMethod = PaymentMethod.COD,
            isPaid = false,
            status = OrderStatus.PACKING,
            orderTimestamp = System.currentTimeMillis() - 90 * 60 * 1000,
            deliveryBoyName = "विकास गुर्जर (Vikas Gurjar)",
            deliveryBoyPhone = "+91 97845 22119",
            pickupOtp = "5521",
            deliveryOtp = "1923",
            packingPhotoTaken = false,
            liveDistanceRemainingKm = 4.2,
            liveEstimatedMinutes = 22
        ),
        Order(
            id = "ord_100",
            orderNumber = "PK-9540",
            customerName = "राजेश शर्मा (Rajesh Sharma)",
            customerPhone = "+91 98291 00221",
            deliveryAddress = "मकान नं. 45, वैशाली नगर, जयपुर - 302021",
            items = listOf(
                OrderItem(
                    product = initialProducts[2],
                    selectedSize = initialProducts[2].sizes[0],
                    selectedColor = initialProducts[2].colors[0],
                    quantity = 1,
                    unitPrice = 450.0
                )
            ),
            subtotal = 450.0,
            deliveryFee = 40.0,
            commissionPercent = 10.0,
            commissionAmount = 45.0,
            sellerPayout = 405.0,
            totalAmount = 490.0,
            paymentMethod = PaymentMethod.UPI,
            isPaid = true,
            status = OrderStatus.DELIVERED,
            orderTimestamp = System.currentTimeMillis() - 24 * 3600 * 1000,
            deliveryBoyName = "सुरेश कुमार (Suresh Kumar)",
            deliveryBoyPhone = "+91 98290 12345",
            pickupOtp = "3311",
            deliveryOtp = "6677",
            packingPhotoTaken = true,
            liveDistanceRemainingKm = 0.0,
            liveEstimatedMinutes = 0
        )
    )

    private val _orders = MutableStateFlow(initialOrders)
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Complaints
    private val initialComplaints = listOf(
        Complaint(
            id = "comp_1",
            orderNumber = "PK-9540",
            productName = "दम बिरयानी व दाल मिट्टी की हांडी",
            issueType = "रास्ते में मामूली दरार (Cracked in Transit)",
            description = "डिलीवरी बॉक्स खोलते ही हांडी के किनारे पर दरार दिखी जिससे पानी रिस रहा है।",
            resolutionType = "नया बर्तन बदलें (Replacement)",
            status = "स्वीकृत (Approved - Replacement dispatched)",
            timestamp = System.currentTimeMillis() - 12 * 3600 * 1000,
            potterShopName = "माटी क्रिएशन्स - मोहन लाल",
            photoSimulated = true,
            refundAmount = 450.0
        )
    )

    private val _complaints = MutableStateFlow(initialComplaints)
    val complaints: StateFlow<List<Complaint>> = _complaints.asStateFlow()

    // Custom requests
    private val initialCustomRequests = listOf(
        CustomOrderRequest(
            id = "cust_1",
            customerName = "राजेश शर्मा",
            customerPhone = "+91 98291 00221",
            potterShopName = "रामेश्वर प्रजापति पॉटरी वर्क्स",
            description = "15 लीटर का बड़ा मंदिर कलश जिसपर मोर व कमल की नक्काशी हो।",
            dimensionsAndCapacity = "ऊंचाई 45 cm, क्षमता 15 L",
            targetBudget = 1200.0,
            status = "कुम्हार ने स्वीकार किया (Accepted by Potter)"
        )
    )

    private val _customRequests = MutableStateFlow(initialCustomRequests)
    val customRequests: StateFlow<List<CustomOrderRequest>> = _customRequests.asStateFlow()

    // Selected Navigation items
    private val _selectedProduct = MutableStateFlow<PotteryProduct?>(null)
    val selectedProduct: StateFlow<PotteryProduct?> = _selectedProduct.asStateFlow()

    private val _selectedTrackingOrder = MutableStateFlow<Order?>(initialOrders[0])
    val selectedTrackingOrder: StateFlow<Order?> = _selectedTrackingOrder.asStateFlow()

    // Action Methods
    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.HINDI) AppLanguage.ENGLISH else AppLanguage.HINDI
    }

    fun setDistanceFilter(km: Int) {
        _selectedDistanceKm.value = km
    }

    fun setCategory(category: PotteryCategory) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCustomerLocation(location: String) {
        _customerLocation.value = location
    }

    fun selectProduct(product: PotteryProduct?) {
        _selectedProduct.value = product
    }

    fun selectTrackingOrder(order: Order?) {
        _selectedTrackingOrder.value = order
    }

    fun toggleWishlist(productId: String) {
        val current = _wishlistIds.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
        } else {
            current.add(productId)
        }
        _wishlistIds.value = current
    }

    fun toggleFavoriteShop(shopId: String) {
        val current = _favoriteShopIds.value.toMutableSet()
        if (current.contains(shopId)) {
            current.remove(shopId)
        } else {
            current.add(shopId)
        }
        _favoriteShopIds.value = current
    }

    // Cart actions
    fun addToCart(product: PotteryProduct, size: ProductVariantSize, color: ClayColor, quantity: Int = 1) {
        val unitPrice = product.basePrice + size.priceDelta
        val existingIndex = _cartItems.value.indexOfFirst {
            it.product.id == product.id && it.selectedSize.sizeNameEn == size.sizeNameEn && it.selectedColor.nameEn == color.nameEn
        }
        val currentList = _cartItems.value.toMutableList()
        if (existingIndex >= 0) {
            val existing = currentList[existingIndex]
            currentList[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            currentList.add(OrderItem(product, size, color, quantity, unitPrice))
        }
        _cartItems.value = currentList
    }

    fun updateCartQuantity(index: Int, newQuantity: Int) {
        val currentList = _cartItems.value.toMutableList()
        if (index in currentList.indices) {
            if (newQuantity <= 0) {
                currentList.removeAt(index)
            } else {
                currentList[index] = currentList[index].copy(quantity = newQuantity)
            }
            _cartItems.value = currentList
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Place Order
    fun placeOrder(
        customerName: String,
        phone: String,
        address: String,
        paymentMethod: PaymentMethod
    ): Order {
        val items = _cartItems.value
        val subtotal = items.sumOf { it.unitPrice * it.quantity }
        val deliveryFee = if (subtotal > 500) 0.0 else 40.0
        val commPercent = _adminCommissionPercent.value
        val commission = subtotal * (commPercent / 100.0)
        val sellerPayout = subtotal - commission
        val total = subtotal + deliveryFee

        val newOrder = Order(
            id = "ord_${System.currentTimeMillis()}",
            orderNumber = "PK-${Random.nextInt(1000, 9999)}",
            customerName = customerName.ifBlank { "ग्राहक (Customer)" },
            customerPhone = phone.ifBlank { "+91 98290 00000" },
            deliveryAddress = address.ifBlank { _customerLocation.value },
            items = items,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            commissionPercent = commPercent,
            commissionAmount = commission,
            sellerPayout = sellerPayout,
            totalAmount = total,
            paymentMethod = paymentMethod,
            isPaid = paymentMethod != PaymentMethod.COD,
            status = OrderStatus.PLACED,
            pickupOtp = "${Random.nextInt(1000, 9999)}",
            deliveryOtp = "${Random.nextInt(1000, 9999)}"
        )

        _orders.value = listOf(newOrder) + _orders.value
        _cartItems.value = emptyList()
        _selectedTrackingOrder.value = newOrder
        return newOrder
    }

    // Advance Order Status (for seller, delivery, and testing)
    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        val list = _orders.value.map { order ->
            if (order.id == orderId) {
                val updated = order.copy(
                    status = newStatus,
                    isPaid = if (newStatus == OrderStatus.DELIVERED) true else order.isPaid,
                    liveDistanceRemainingKm = when (newStatus) {
                        OrderStatus.OUT_FOR_DELIVERY -> 1.2
                        OrderStatus.DELIVERED -> 0.0
                        else -> order.liveDistanceRemainingKm
                    },
                    liveEstimatedMinutes = when (newStatus) {
                        OrderStatus.OUT_FOR_DELIVERY -> 6
                        OrderStatus.DELIVERED -> 0
                        else -> order.liveEstimatedMinutes
                    }
                )
                if (_selectedTrackingOrder.value?.id == orderId) {
                    _selectedTrackingOrder.value = updated
                }
                updated
            } else {
                order
            }
        }
        _orders.value = list
    }

    // Seller: Add New Product
    fun addProduct(product: PotteryProduct) {
        _products.value = listOf(product) + _products.value
    }

    // Seller: Register or Update Shop
    fun registerShop(shop: PotteryShop) {
        _shops.value = listOf(shop) + _shops.value
    }

    // Customer: Submit Complaint
    fun submitComplaint(
        orderNumber: String,
        productName: String,
        issueType: String,
        description: String,
        resolutionType: String,
        potterShopName: String,
        refundAmount: Double
    ) {
        val newComplaint = Complaint(
            id = "comp_${System.currentTimeMillis()}",
            orderNumber = orderNumber,
            productName = productName,
            issueType = issueType,
            description = description,
            resolutionType = resolutionType,
            status = "Pending (समीक्षाधीन)",
            timestamp = System.currentTimeMillis(),
            potterShopName = potterShopName,
            photoSimulated = true,
            refundAmount = refundAmount
        )
        _complaints.value = listOf(newComplaint) + _complaints.value
    }

    // Customer: Submit Custom Order Request
    fun submitCustomOrder(
        customerName: String,
        customerPhone: String,
        shopName: String,
        description: String,
        dimensions: String,
        budget: Double
    ) {
        val request = CustomOrderRequest(
            id = "cust_${System.currentTimeMillis()}",
            customerName = customerName,
            customerPhone = customerPhone,
            potterShopName = shopName,
            description = description,
            dimensionsAndCapacity = dimensions,
            targetBudget = budget,
            status = "Sent to Potter (कुम्हार को भेजा गया)"
        )
        _customRequests.value = listOf(request) + _customRequests.value
    }

    // Delivery: Confirm pre-pickup photo
    fun confirmPackingPhoto(orderId: String) {
        _orders.value = _orders.value.map {
            if (it.id == orderId) it.copy(packingPhotoTaken = true) else it
        }
    }

    // Admin: Set Commission
    fun setAdminCommission(percent: Double) {
        _adminCommissionPercent.value = percent
    }

    // Admin: Approve/Reject Shop
    fun toggleShopApproval(shopId: String) {
        _shops.value = _shops.value.map {
            if (it.id == shopId) it.copy(isApproved = !it.isApproved) else it
        }
    }

    // Admin: Update Complaint Status
    fun updateComplaintStatus(complaintId: String, newStatus: String) {
        _complaints.value = _complaints.value.map {
            if (it.id == complaintId) it.copy(status = newStatus) else it
        }
    }
}
