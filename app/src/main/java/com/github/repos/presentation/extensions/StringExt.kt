package com.github.repos.presentation.extensions

import android.app.Activity
import androidx.preference.PreferenceManager
import com.github.repos.Const
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
    val dateFormatWS = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // 2020-10-03 14:00:49

    val dateFormatWS2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") // 2020-10-03 14:00:49
    val dateFormatWS3 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // 2020-10-03 14:00:49
    val dateFormatWS4 = SimpleDateFormat("dd/MM/yyyy'T'-'T'HH:mm:ss") // 2020-10-03 14:00:49
    val dateFormatBKM = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'")

    val dateFormatRange = SimpleDateFormat("yyyy/MM/dd") // 2020/10/03

    val dateFormatRangeBKM = SimpleDateFormat("yyyy-MM-dd") // 2020/10/03

    val dateFormatRange2 = SimpleDateFormat("dd/MM/yyyy") // 03/10/2020

    val dateFormatRange3 = SimpleDateFormat("dd-MM-yyyy HH:mm:ss") // 21-12-2020 14:00:49

    fun changeDateFormat(dateString: String): String {
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

    fun validateCitizenshipID(id: Int): Boolean { // Bir TC Kimlik Numarası'nın geçerli olup olmadığını kontrol eder
        val digits = id.toString().map { it.toString().toInt() }
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

    fun getLastTenChar(str: String): String { // Verilen metindeki özel karakterleri ve boşlukları temizler. Temizlenen metnin son 10 karakterini döndürür. Eğer metin 10 karakterden kısa ise, boş bir dize döner.
        var formattedStr = str.replace("[-\\[\\]^/,'*:.!><~@#$%=?|\"\\\\()]+".toRegex(), "")
        formattedStr = formattedStr.replace(" ", "")
        return if (formattedStr.length == 10) {
            formattedStr
        } else if (formattedStr.length > 10) {
            formattedStr.substring(formattedStr.length - 10)
        } else {
            ""
        }
    }

    fun generateRandomUUID(): String {
        return UUID.randomUUID().toString()
    }

    fun getFormattedAmount(fee: Float): String {
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

    fun getBalanceText(fee: Float): String {
        val locale = Locale("en", "UK")
        val symbols = DecimalFormatSymbols(locale)
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'
        val pattern = "###,###.##"
        val decimalFormat = DecimalFormat(pattern, symbols)
        val format = decimalFormat.format(fee.toDouble())
        val parts = format.split(",".toRegex()).toTypedArray()
        var part1 = "0"
        if (parts.isNotEmpty()) {
            part1 = parts[0]
        }
        return part1
    }

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
        val currencyName: String = when (currencyCode) {
            "TRY" -> {
                "₺"
            }

            "USD" -> {
                "$"
            }

            "EUR" -> {
                "€"
            }

            else -> {
                currencyCode
            }
        }
        return currencyName
    }

    fun loadJSONFromAsset(
        fileAddress: String?,
        activity: Activity
    ): String? {
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

    fun saveLoginSuccess(activity: Activity?) { // Kullanıcı başarılı bir şekilde giriş yaptığında, giriş zamanını kaydeder
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity!!)
        val editor = sharedPref.edit()
        editor.putString(
            Const.LOGIN_SUCCESS_TIME,
            getDateStr(Date(), dateFormatRange3)
        )
        editor.commit()
    }

    fun saveLoginFailed(activity: Activity?) { // Kullanıcı başarısız giriş yaptığında, giriş zamanını kaydeder
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity!!)
        val editor = sharedPref.edit()
        editor.putString(
            Const.LOGIN_FAILED_TIME,
            getDateStr(Date(), dateFormatRange3)
        )
        editor.commit()
    }

    fun getSuccessLoginTimeStr(activity: Activity?): String? { // Başarılı giriş zamanını almak için kullanılır
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity!!)
        return sharedPref.getString(Const.LOGIN_SUCCESS_TIME, "")
    }

    fun getFailedLoginTimeStr(activity: Activity?): String? { // Başarısız giriş zamanını almak için kullanılır
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity!!)
        return sharedPref.getString(Const.LOGIN_FAILED_TIME, "")
    }

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
        var formattedCardNo: String?
        formattedCardNo = cardNo?.replace("[a-zA-Z]".toRegex(), "*").toString()
        formattedCardNo = formattedCardNo.replace("(.{4})".toRegex(), "$1 ")
        return formattedCardNo
    }

    fun maskPhoneNumber(phoneNumber: String): String { // Telefon numarasını gizler, belirli bir kısmını * ile değiştirir
        if (phoneNumber.length >= 5) {
            val visibleLength = "+90".length
            val maskedLength = phoneNumber.length - visibleLength - 2
            val maskedString = "*".repeat(maskedLength)
            return phoneNumber.substring(0, visibleLength) + maskedString + phoneNumber.takeLast(2)
        }
        return phoneNumber
    }

    fun trimmedPhoneNumber(phoneNumber: String): String { // Telefon numarasının ülke kodunu kaldırır
        val countryCodeLength = removePlusIfFirstChar("+90").length
        return phoneNumber.substring(countryCodeLength)
    }
}