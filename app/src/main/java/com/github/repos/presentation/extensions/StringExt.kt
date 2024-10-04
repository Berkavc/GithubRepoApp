package com.github.repos.presentation.extensions

import android.app.Activity
import androidx.preference.PreferenceManager
import com.github.repos.Const
import com.github.repos.MainApp.Companion.getMainApp
import java.io.IOException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

object StringExt {

    @JvmStatic
    var fcmToken = "" // Token saklar

    @JvmField
    val dateFormatWS = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // 2020-10-03 14:00:49

    @JvmField
    val dateFormatWS2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") // 2020-10-03 14:00:49
    val dateFormatWS3 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // 2020-10-03 14:00:49
    val dateFormatWS4 = SimpleDateFormat("dd/MM/yyyy'T'-'T'HH:mm:ss") // 2020-10-03 14:00:49
    val dateFormatBKM = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'")

    @JvmField
    val dateFormatRange = SimpleDateFormat("yyyy/MM/dd") // 2020/10/03

    @JvmField
    val dateFormatRangeBKM = SimpleDateFormat("yyyy-MM-dd") // 2020/10/03

    @JvmField
    val dateFormatRange2 = SimpleDateFormat("dd/MM/yyyy") // 03/10/2020

    @JvmField
    val dateFormatRange3 = SimpleDateFormat("dd-MM-yyyy HH:mm:ss") // 21-12-2020 14:00:49

    fun changeDateFormat(dateString: String): String { // yyyy-MM-dd'T'HH:mm:ssZ formatından dd-MM-yyyy HH:mm:ss formatına dönüştürür
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        val date = dateFormatter.parse(dateString)
        val newFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date)
        return newFormat
    }

    fun isVKN(input: String): Boolean { // 10 haneli VKN olup olmadığını kontrol eder
        val vknRegex = "/^[0-9]{10}$/"
        return Regex(vknRegex).matches(input)
    }

    fun isEmail(input: String): Boolean { // E-posta adresinin geçerli olup olmadığını kontrol eder
        val emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"
        return Regex(emailRegex).matches(input)
    }

    fun validateCitizenshipID(ID: Int): Boolean { // Bir TC Kimlik Numarası'nın geçerli olup olmadığını kontrol eder
        val digits = ID.toString().map { it.toString().toInt() }
        return if (digits.size == 11 && digits[0] != 0) {
            val first = (digits[0] + digits[2] + digits[4] + digits[6] + digits[8]) * 7
            val second = (digits[1] + digits[3] + digits[5] + digits[7])

            val digit10 = (first - second) % 10
            val digit11 =
                (digits[0] + digits[1] + digits[2] + digits[3] + digits[4] + digits[5] + digits[6] + digits[7] + digits[8] + digits[9]) % 10

            (digits[10] == digit11) && (digits[9] == digit10)
        } else {
            false
        }
    }

    @JvmStatic
    fun getDateStr(
        date: Date?,
        format: SimpleDateFormat
    ): String { // Verilen tarihi belirlenen formatta bir metin olarak döndürür
        var stringVal = ""
        try {
            stringVal = format.format(date)
        } catch (ex: Exception) {
        }
        return stringVal
    }

    fun getTransactionItemDateStr(
        dateStr: String?,
        dateFormat: SimpleDateFormat
    ): String { // Verilen tarih dizesini dd-MM-yyyy formatında bir dize olarak geri döndürür
        val date = convertStringToDate(dateStr, dateFormat)
        return SimpleDateFormat("dd-MM-yyyy").format(date)
    }

    @JvmStatic
    fun convertStringToDate(
        str: String?,
        format: SimpleDateFormat
    ): Date { // Tarih dizesini UTC zaman diliminde bir Date objesine çevirir
        format.timeZone = TimeZone.getTimeZone("UTC")
        var date = Date()
        try {
            date = format.parse(str)
            println(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    @JvmStatic
    fun getLastTenChar(s: String): String { // Verilen metindeki özel karakterleri ve boşlukları temizler. Temizlenen metnin son 10 karakterini döndürür. Eğer metin 10 karakterden kısa ise, boş bir dize döner.
        var p = s.replace("[-\\[\\]^/,'*:.!><~@#$%=?|\"\\\\()]+".toRegex(), "")
        p = p.replace(" ", "")
        return if (p.length == 10) {
            p
        } else if (p.length > 10) {
            p.substring(p.length - 10)
        } else {
            ""
        }
    }

    @JvmStatic
    fun generateRandomUUID(): String { // Rastgele bir UUID oluşturur
        return UUID.randomUUID().toString()
    }

    @JvmStatic
    fun getFormattedAmount(fee: Float): String { // Verilen fee değerini, belirli bir biçimde formatlayarak metin olarak döndürür
        val locale = Locale("en", "UK")
        val symbols = DecimalFormatSymbols(locale)
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'
        val pattern = "###,###.##"
        val decimalFormat = DecimalFormat(pattern, symbols)
        var format = decimalFormat.format(fee.toDouble())
        if (fee % 1 == 0f) {
            format += ",00"
        }
        return format
    }

    @JvmStatic
    fun getBalanceText(fee: Float): String { // Verilen fee değerinin sadece tam sayı kısmını döndürür
        val locale = Locale("en", "UK")
        val symbols = DecimalFormatSymbols(locale)
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'
        val pattern = "###,###.##"
        val decimalFormat = DecimalFormat(pattern, symbols)
        val format = decimalFormat.format(fee.toDouble())
        val parts = format.split(",".toRegex()).toTypedArray()
        var part1 = "0"
        var part2 = "00"
        if (parts.size > 0) {
            part1 = parts[0]
        }
        if (parts.size > 1) {
            part2 = parts[1]
        }
        return part1
    }

    @JvmStatic
    fun getDecimalText(fee: Float): String { // Verilen fee değerinin virgülden sonraki ondalık kısmını alır ve iki basamaklı hale getirir
        val locale = Locale("en", "UK")
        val symbols = DecimalFormatSymbols(locale)
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'
        val pattern = "###,###.##"
        val decimalFormat = DecimalFormat(pattern, symbols)
        val format = decimalFormat.format(fee.toDouble())
        val parts = format.split(",".toRegex()).toTypedArray()
        var part1 = "0"
        var part2 = "00"
        if (parts.size > 0) {
            part1 = parts[0]
        }
        if (parts.size > 1) {
            part2 = parts[1]
        }

        // Eğer virgülden sonraki kısım 1 karakterden kısa ise bir sıfır daha ekleyerek iki basamağa tamamla
        if (part2.length < 2) {
            part2 = part2.padEnd(2, '0')
        }

        return ",$part2"
    }

    fun getCurrencyIcon(currencyCode: String): String { // Verilen para birimi koduna göre ilgili simgeyi döndürür
        var currencyName = ""
        currencyName = if (currencyCode == "TRY") {
            "₺"
        } else if (currencyCode == "USD") {
            "$"
        } else if (currencyCode == "EUR") {
            "€"
        } else {
            currencyCode
        }
        return currencyName
    }

    fun loadJSONFromAsset(
        fileAddress: String?,
        activity: Activity
    ): String? { // Assets bir JSON dosyasını okur ve string formatında döndürür
        var json: String? = null
        json = try {
            val `is` = activity.assets.open(fileAddress!!)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    @JvmStatic
    fun saveLoginSuccess(activity: Activity?) { // Kullanıcı başarılı bir şekilde giriş yaptığında, giriş zamanını kaydeder
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity!!)
        val editor = sharedPref.edit()
        editor.putString(
            Const.LOGIN_SUCCESS_TIME,
            StringExt.getDateStr(Date(), StringExt.dateFormatRange3)
        )
        editor.commit()
    }

    @JvmStatic
    fun saveLoginFailed(activity: Activity?) { // Kullanıcı başarısız giriş yaptığında, giriş zamanını kaydeder
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity!!)
        val editor = sharedPref.edit()
        editor.putString(
            Const.LOGIN_FAILED_TIME,
            StringExt.getDateStr(Date(), StringExt.dateFormatRange3)
        )
        editor.commit()
    }

    @JvmStatic
    fun getSuccessLoginTimeStr(activity: Activity?): String? { // Başarılı giriş zamanını almak için kullanılır
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity!!)
        return sharedPref.getString(Const.LOGIN_SUCCESS_TIME, "")
    }

    @JvmStatic
    fun getFailedLoginTimeStr(activity: Activity?): String? { // Başarısız giriş zamanını almak için kullanılır
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity!!)
        return sharedPref.getString(Const.LOGIN_FAILED_TIME, "")
    }

    @JvmStatic
    fun getHiddenCardNoStyled(cardNo: String): String { // Kart numarasını maskeler
        val hiddenStr = cardNo.replace("[^\\d.]".toRegex(), "*")
        return hiddenStr.replace("....".toRegex(), "$0 ")
    }

    fun removePlusIfFirstChar(input: String): String { // Eğer girilen string + ile başlıyorsa bu karakteri kaldırır.
        return if (input.startsWith("+")) {
            input.substring(1)
        } else {
            input
        }
    }

    fun getFormattedCardNo(cardNo: String?): String { // Kart numarasını gizlemek ve belirli bir formatta döndürür
        var formattedCardNo: String? = ""
        formattedCardNo = cardNo?.replace("[a-zA-Z]".toRegex(), "*").toString()
        formattedCardNo = formattedCardNo.replace("(.{4})".toRegex(), "$1 ")
        return formattedCardNo
    }

    fun maskPhoneNumber(phoneNumber: String): String { // Telefon numarasını gizler, belirli bir kısmını * ile değiştirir
        if (phoneNumber.length >= 5) {
            val visibleLength = getMainApp()?.getUserPhoneCountryCode()?.length ?: 0
            val maskedLength = phoneNumber.length - visibleLength - 2
            val maskedString = "*".repeat(maskedLength)
            return phoneNumber.substring(0, visibleLength) + maskedString + phoneNumber.takeLast(2)
        }
        return phoneNumber
    }

    fun trimmedPhoneNumber(phoneNumber: String): String { // Telefon numarasının ülke kodunu kaldırır
        val countryCodeLength =
            getMainApp()?.getUserPhoneCountryCode()
                ?.let { StringExt.removePlusIfFirstChar(it) }?.length ?: 2
        return phoneNumber.substring(countryCodeLength)
    }

}