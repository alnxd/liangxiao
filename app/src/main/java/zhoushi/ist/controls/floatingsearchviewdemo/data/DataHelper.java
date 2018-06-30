package zhoushi.ist.controls.floatingsearchviewdemo.data;

/**
 * Copyright (C) 2015 Ari C.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.widget.Filter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import zhoushi.ist.dal.Dal_Act;
import zhoushi.ist.dal.Dal_Org;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.Act;
import zhoushi.ist.entity.Org;
import zhoushi.ist.network.NetProtocol;

public class DataHelper {

    public static int m_nDataType = NetProtocol.PULLALLID_ORG;

    private static List<ColorSuggestion> sOrgSuggestions =
            new ArrayList<>();
    private static List<ColorSuggestion> sActSuggestions =
            new ArrayList<>();

    public interface OnFindSuggestionsListener {
        void onResults(List<ColorSuggestion> results);
    }

    //初始化推荐列表
    public static void initSuggestionsList(int nType) {
        m_nDataType = nType;
        sOrgSuggestions.clear();
        sActSuggestions.clear();

        Dal_Org dOrg = DataManager.getInstance().getM_dataOrg();
        Iterator iter = dOrg.getM_listData().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Org org = (Org)entry.getValue();
            ColorSuggestion cs = new ColorSuggestion(org.getId(),org.getName());
            sOrgSuggestions.add(cs);
        }
        Dal_Act dAct = DataManager.getInstance().getM_dataAct();
        iter = dAct.getM_listData().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Act act = (Act)entry.getValue();
            ColorSuggestion cs = new ColorSuggestion(act.getId(),act.getName());
            sActSuggestions.add(cs);
        }
    }

    private static List<ColorSuggestion> getCurrSuggestions()
    {
       return (m_nDataType == NetProtocol.PULLALLID_ORG)?sOrgSuggestions:sActSuggestions;
    }

    public static List<ColorSuggestion> getHistory(Context context, int count) {

        List<ColorSuggestion> suggestionList = new ArrayList<>();
        ColorSuggestion colorSuggestion;
        List<ColorSuggestion> sList = getCurrSuggestions();
        for (int i = 0; i < sList.size(); i++) {
            colorSuggestion = sList.get(i);
            colorSuggestion.setIsHistory(true);
            suggestionList.add(colorSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        List<ColorSuggestion> sList = getCurrSuggestions();
        for (ColorSuggestion colorSuggestion : sList) {
            colorSuggestion.setIsHistory(false);
        }
    }

    public static void findSuggestions( String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelper.resetSuggestionsHistory();
                List<ColorSuggestion> sList = getCurrSuggestions();
                List<ColorSuggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (ColorSuggestion suggestion : sList) {
                        if (suggestion.getBody().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<ColorSuggestion>() {
                    @Override
                    public int compare(ColorSuggestion lhs, ColorSuggestion rhs) {
                        return lhs.getIsHistory() ? -1 : 0;
                    }
                });
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<ColorSuggestion>) results.values);
                }
            }
        }.filter(query);

    }

//    private static String loadJson(Context context) {
//
//        String jsonString;
//
//        try {
//            InputStream is = context.getAssets().open(COLORS_FILE_NAME);
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            jsonString = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//
//        return jsonString;
//    }
//
//    private static List<ColorWrapper> deserializeColors(String jsonString) {
//
//        Gson gson = new Gson();
//
//        Type collectionType = new TypeToken<List<ColorWrapper>>() {
//        }.getType();
//        return gson.fromJson(jsonString, collectionType);
//    }

}