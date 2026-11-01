package com.lunamistica.app

/** País con su código telefónico internacional. */
data class Country(val iso: String, val name: String, val dialCode: String) {
    /** Bandera en emoji, construida a partir del código ISO (p. ej. "CO" → 🇨🇴). */
    val flag: String
        get() = buildString {
            for (c in iso.uppercase()) {
                val v = 0x1F1E6 + (c - 'A') - 0x10000
                append((0xD800 + (v shr 10)).toChar())
                append((0xDC00 + (v and 0x3FF)).toChar())
            }
        }

    val label: String get() = "$flag +$dialCode"
}

object Countries {
    val all: List<Country> = listOf(
        Country("AF", "Afganistán", "93"), Country("AL", "Albania", "355"), Country("DE", "Alemania", "49"),
        Country("AD", "Andorra", "376"), Country("AO", "Angola", "244"), Country("AG", "Antigua y Barbuda", "1"),
        Country("SA", "Arabia Saudita", "966"), Country("DZ", "Argelia", "213"), Country("AR", "Argentina", "54"),
        Country("AM", "Armenia", "374"), Country("AW", "Aruba", "297"), Country("AU", "Australia", "61"),
        Country("AT", "Austria", "43"), Country("AZ", "Azerbaiyán", "994"), Country("BS", "Bahamas", "1"),
        Country("BH", "Baréin", "973"), Country("BD", "Bangladés", "880"), Country("BB", "Barbados", "1"),
        Country("BE", "Bélgica", "32"), Country("BZ", "Belice", "501"), Country("BJ", "Benín", "229"),
        Country("BY", "Bielorrusia", "375"), Country("BO", "Bolivia", "591"), Country("BA", "Bosnia y Herzegovina", "387"),
        Country("BW", "Botsuana", "267"), Country("BR", "Brasil", "55"), Country("BN", "Brunéi", "673"),
        Country("BG", "Bulgaria", "359"), Country("BF", "Burkina Faso", "226"), Country("BI", "Burundi", "257"),
        Country("BT", "Bután", "975"), Country("CV", "Cabo Verde", "238"), Country("KH", "Camboya", "855"),
        Country("CM", "Camerún", "237"), Country("CA", "Canadá", "1"), Country("QA", "Catar", "974"),
        Country("TD", "Chad", "235"), Country("CL", "Chile", "56"), Country("CN", "China", "86"),
        Country("CY", "Chipre", "357"), Country("CO", "Colombia", "57"), Country("KM", "Comoras", "269"),
        Country("CG", "Congo", "242"), Country("CD", "Congo (Rep. Dem.)", "243"), Country("KP", "Corea del Norte", "850"),
        Country("KR", "Corea del Sur", "82"), Country("CI", "Costa de Marfil", "225"), Country("CR", "Costa Rica", "506"),
        Country("HR", "Croacia", "385"), Country("CU", "Cuba", "53"), Country("CW", "Curazao", "599"),
        Country("DK", "Dinamarca", "45"), Country("DM", "Dominica", "1"), Country("EC", "Ecuador", "593"),
        Country("EG", "Egipto", "20"), Country("SV", "El Salvador", "503"), Country("AE", "Emiratos Árabes Unidos", "971"),
        Country("ER", "Eritrea", "291"), Country("SK", "Eslovaquia", "421"), Country("SI", "Eslovenia", "386"),
        Country("ES", "España", "34"), Country("US", "Estados Unidos", "1"), Country("EE", "Estonia", "372"),
        Country("SZ", "Esuatini", "268"), Country("ET", "Etiopía", "251"), Country("PH", "Filipinas", "63"),
        Country("FI", "Finlandia", "358"), Country("FJ", "Fiyi", "679"), Country("FR", "Francia", "33"),
        Country("GA", "Gabón", "241"), Country("GM", "Gambia", "220"), Country("GE", "Georgia", "995"),
        Country("GH", "Ghana", "233"), Country("GI", "Gibraltar", "350"), Country("GD", "Granada", "1"),
        Country("GR", "Grecia", "30"), Country("GL", "Groenlandia", "299"), Country("GP", "Guadalupe", "590"),
        Country("GU", "Guam", "1"), Country("GT", "Guatemala", "502"), Country("GF", "Guayana Francesa", "594"),
        Country("GN", "Guinea", "224"), Country("GQ", "Guinea Ecuatorial", "240"), Country("GW", "Guinea-Bisáu", "245"),
        Country("GY", "Guyana", "592"), Country("HT", "Haití", "509"), Country("HN", "Honduras", "504"),
        Country("HK", "Hong Kong", "852"), Country("HU", "Hungría", "36"), Country("IN", "India", "91"),
        Country("ID", "Indonesia", "62"), Country("IQ", "Irak", "964"), Country("IR", "Irán", "98"),
        Country("IE", "Irlanda", "353"), Country("IS", "Islandia", "354"), Country("KY", "Islas Caimán", "1"),
        Country("MH", "Islas Marshall", "692"), Country("SB", "Islas Salomón", "677"), Country("IL", "Israel", "972"),
        Country("IT", "Italia", "39"), Country("JM", "Jamaica", "1"), Country("JP", "Japón", "81"),
        Country("JO", "Jordania", "962"), Country("KZ", "Kazajistán", "7"), Country("KE", "Kenia", "254"),
        Country("KG", "Kirguistán", "996"), Country("KI", "Kiribati", "686"), Country("KW", "Kuwait", "965"),
        Country("LA", "Laos", "856"), Country("LS", "Lesoto", "266"), Country("LV", "Letonia", "371"),
        Country("LB", "Líbano", "961"), Country("LR", "Liberia", "231"), Country("LY", "Libia", "218"),
        Country("LI", "Liechtenstein", "423"), Country("LT", "Lituania", "370"), Country("LU", "Luxemburgo", "352"),
        Country("MO", "Macao", "853"), Country("MK", "Macedonia del Norte", "389"), Country("MG", "Madagascar", "261"),
        Country("MY", "Malasia", "60"), Country("MW", "Malaui", "265"), Country("MV", "Maldivas", "960"),
        Country("ML", "Malí", "223"), Country("MT", "Malta", "356"), Country("MA", "Marruecos", "212"),
        Country("MQ", "Martinica", "596"), Country("MU", "Mauricio", "230"), Country("MR", "Mauritania", "222"),
        Country("MX", "México", "52"), Country("FM", "Micronesia", "691"), Country("MD", "Moldavia", "373"),
        Country("MC", "Mónaco", "377"), Country("MN", "Mongolia", "976"), Country("ME", "Montenegro", "382"),
        Country("MZ", "Mozambique", "258"), Country("MM", "Myanmar", "95"), Country("NA", "Namibia", "264"),
        Country("NR", "Nauru", "674"), Country("NP", "Nepal", "977"), Country("NI", "Nicaragua", "505"),
        Country("NE", "Níger", "227"), Country("NG", "Nigeria", "234"), Country("NO", "Noruega", "47"),
        Country("NC", "Nueva Caledonia", "687"), Country("NZ", "Nueva Zelanda", "64"), Country("OM", "Omán", "968"),
        Country("NL", "Países Bajos", "31"), Country("PK", "Pakistán", "92"), Country("PW", "Palaos", "680"),
        Country("PS", "Palestina", "970"), Country("PA", "Panamá", "507"), Country("PG", "Papúa Nueva Guinea", "675"),
        Country("PY", "Paraguay", "595"), Country("PE", "Perú", "51"), Country("PF", "Polinesia Francesa", "689"),
        Country("PL", "Polonia", "48"), Country("PT", "Portugal", "351"), Country("PR", "Puerto Rico", "1"),
        Country("GB", "Reino Unido", "44"), Country("CF", "República Centroafricana", "236"), Country("CZ", "República Checa", "420"),
        Country("DO", "República Dominicana", "1"), Country("RE", "Reunión", "262"), Country("RW", "Ruanda", "250"),
        Country("RO", "Rumania", "40"), Country("RU", "Rusia", "7"), Country("WS", "Samoa", "685"),
        Country("KN", "San Cristóbal y Nieves", "1"), Country("SM", "San Marino", "378"), Country("VC", "San Vicente y las Granadinas", "1"),
        Country("LC", "Santa Lucía", "1"), Country("ST", "Santo Tomé y Príncipe", "239"), Country("SN", "Senegal", "221"),
        Country("RS", "Serbia", "381"), Country("SC", "Seychelles", "248"), Country("SL", "Sierra Leona", "232"),
        Country("SG", "Singapur", "65"), Country("SX", "Sint Maarten", "1"), Country("SY", "Siria", "963"),
        Country("SO", "Somalia", "252"), Country("LK", "Sri Lanka", "94"), Country("ZA", "Sudáfrica", "27"),
        Country("SD", "Sudán", "249"), Country("SS", "Sudán del Sur", "211"), Country("SE", "Suecia", "46"),
        Country("CH", "Suiza", "41"), Country("SR", "Surinam", "597"), Country("TH", "Tailandia", "66"),
        Country("TW", "Taiwán", "886"), Country("TZ", "Tanzania", "255"), Country("TJ", "Tayikistán", "992"),
        Country("TL", "Timor Oriental", "670"), Country("TG", "Togo", "228"), Country("TO", "Tonga", "676"),
        Country("TT", "Trinidad y Tobago", "1"), Country("TN", "Túnez", "216"), Country("TM", "Turkmenistán", "993"),
        Country("TR", "Turquía", "90"), Country("TV", "Tuvalu", "688"), Country("UA", "Ucrania", "380"),
        Country("UG", "Uganda", "256"), Country("UY", "Uruguay", "598"), Country("UZ", "Uzbekistán", "998"),
        Country("VU", "Vanuatu", "678"), Country("VA", "Vaticano", "39"), Country("VE", "Venezuela", "58"),
        Country("VN", "Vietnam", "84"), Country("YE", "Yemen", "967"), Country("DJ", "Yibuti", "253"),
        Country("ZM", "Zambia", "260"), Country("ZW", "Zimbabue", "263"),
    ).sortedBy { sortKey(it.name) }

    /** Países más usados, que se muestran primero en la lista. */
    val popular: List<Country> = listOf("CO", "US", "MX", "ES", "VE", "EC", "PE", "AR", "CL")
        .map { iso -> all.first { it.iso == iso } }

    val default: Country = all.first { it.iso == "CO" }

    fun byIso(iso: String?): Country? = all.firstOrNull { it.iso == iso }

    /** Busca por nombre (sin importar tildes) o por código (con o sin "+"). */
    fun search(query: String): List<Country> {
        val q = sortKey(query.trim().removePrefix("+"))
        if (q.isEmpty()) return all
        return all.filter { sortKey(it.name).contains(q) || it.dialCode.startsWith(q) || it.iso.lowercase() == q }
    }

    private fun sortKey(s: String): String = s.lowercase().map { c -> Numerology.normalize(c)?.lowercaseChar() ?: c }.joinToString("")
}
