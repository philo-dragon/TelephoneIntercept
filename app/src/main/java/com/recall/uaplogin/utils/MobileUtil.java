package com.recall.uaplogin.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.List;


public class MobileUtil {

    private static final String TAG = MobileUtil.class.getSimpleName();

    private static List<String> areaCodes = Arrays.asList("10", "21", "22", "23", "20", "24", "25", "27", "28", "29", "311", "312", "313", "314", "315", "316", "317", "318", "319", "310", "335", "349", "351", "352", "353", "354", "355", "356", "357", "358", "359", "350", "371", "372", "373", "374", "375", "376", "377", "379", "370", "391", "392", "393", "394", "395", "396", "398", "411", "412", "415", "416", "417", "418", "419", "421", "427", "429", "431", "432", "433", "434", "435", "436", "437", "438", "439", "451", "452", "453", "454", "455", "456", "457", "458", "459", "464", "467", "468", "469", "471", "472", "473", "474", "475", "476", "477", "478", "479", "470", "482", "483", "511", "512", "513", "514", "515", "516", "517", "518", "519", "510", "523", "527", "531", "532", "533", "534", "535", "536", "537", "538", "539", "530", "543", "546", "631", "632", "633", "635", "551", "552", "553", "554", "555", "556", "557", "558", "559", "550", "561", "562", "563", "564", "566", "571", "572", "573", "574", "575", "576", "577", "578", "579", "570", "580", "591", "592", "593", "594", "595", "596", "597", "598", "599", "631", "632", "633", "635", "662", "663", "668", "660", "711", "712", "713", "714", "715", "716", "717", "718", "719", "710", "722", "724", "728", "731", "734", "735", "736", "737", "738", "739", "730", "743", "744", "745", "746", "751", "752", "753", "754", "755", "756", "757", "758", "759", "750", "762", "763", "766", "768", "769", "760", "662", "663", "668", "660", "771", "772", "773", "774", "775", "776", "777", "778", "779", "770", "791", "792", "793", "794", "795", "796", "797", "798", "799", "790", "701", "812", "813", "816", "817", "818", "825", "826", "827", "831", "832", "833", "834", "835", "836", "837", "838", "839", "830", "851", "852", "853", "854", "855", "856", "857", "858", "859", "871", "872", "873", "874", "875", "876", "877", "878", "879", "870", "883", "886", "887", "888", "691", "692", "891", "892", "893", "894", "895", "896", "897", "898", "911", "912", "913", "914", "915", "916", "917", "919", "931", "932", "933", "934", "935", "936", "937", "938", "939", "930", "941", "943", "951", "952", "953", "954", "955", "971", "972", "973", "974", "975", "976", "977", "979", "970", "991", "992", "993", "994", "995", "996", "997", "998", "999", "990", "901", "902", "903", "906", "908", "909");

    public static String getNumber(String phoneNo) {
        if (TextUtils.isEmpty(phoneNo)) {
            return null;
        }
        //如果是以“+86”或者“0”开头，为有前缀号码，需要去掉区号。如果是国际号码，比如“+61497308543”则不做修正。
        if (phoneNo.startsWith("+86")) {
            //以+开头，如果以+861；+862开头，则读取前5位，其他的读取前6位，按照中国区号表匹配，匹配成功后去掉区号。
            // 如果前三位是+861X(X不是0)则去掉前3位+86即可。如果匹配不到则为国际号码，不做修正。
            if (phoneNo.startsWith("+861")
                    || phoneNo.startsWith("+862")) {
                try {
                    if (!phoneNo.startsWith("+8610")) {
                        return phoneNo.substring(3);//如果前三位是+861X(X不是0)则去掉前3位+86即可
                    } else {
                        String areaCode = phoneNo.substring(0, 5);
                        if (areaCodes.contains(areaCode.substring(3))) {
                            return phoneNo.substring(5);//如果匹配不到则去掉首位0即可。
                        } else {
                            return phoneNo;
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage() + " =========== " + phoneNo);
                }
            } else {
                try {
                    String areaCode = phoneNo.substring(0, 6);
                    if (areaCodes.contains(areaCode.substring(3))) {
                        return phoneNo.substring(6);
                    } else {
                        return phoneNo;
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage() + " =========== " + phoneNo);
                }
            }
        } else if (phoneNo.startsWith("0")) {
            // 以0开头，如果以01；02开头，则读取前3位，其他的读取前4位，按照中国区号表匹配，匹配成功后去掉区号。
            // 如果前三位是01X(X不是0)则去掉首位0即可。如果匹配不到则去掉首位0即可。
            if (phoneNo.startsWith("01")
                    || phoneNo.startsWith("02")) {
                try {
                    // 01067013137
                    if (!phoneNo.startsWith("010")) {
                        return phoneNo.substring(1);//如果前三位是01X(X不是0)则去掉首位0即可
                    } else {
                        String areaCode = phoneNo.substring(1, 3);
                        if (areaCodes.contains(areaCode)) {
                            return phoneNo.substring(3);
                        } else {
                            return phoneNo;
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage() + " =========== " + phoneNo);
                }

            } else {
                try {
                    String areaCode = phoneNo.substring(1, 4);
                    if (areaCodes.contains(areaCode)) {
                        return phoneNo.substring(4);
                    } else {
                        return phoneNo;
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage() + " =========== " + phoneNo);
                }
            }
        } else {
            return phoneNo;
        }

        return phoneNo;
    }

}
