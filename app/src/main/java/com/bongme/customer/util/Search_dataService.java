package service;

import application.sp.helper.Search_LogHelper;
import application.sp.helper.Search_dataHelper;
import application.sp.resource.search_data;
import platform.db.Expression;
import platform.db.LOG_OP;
import platform.db.REL_OP;
import platform.helper.HelperUtils;
import platform.resource.BaseResource;
import platform.util.ApplicationException;
import platform.util.ExceptionSeverity;
import platform.util.Util;
import platform.webservice.ServletContext;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by ajay.nema on 27-06-2016.
 */
public class Search_dataService extends SPService {
    public Search_dataService() {

        super(Search_dataHelper.getInstance(), new search_data());
    }
    private enum QueryTypes {
        //Expects emailId. Returns the profile associated with this emailId.
        QUERY_SEARCH;
    }

    public BaseResource[] getQuery(ServletContext ctx, String queryId, Map<String, Object> map) throws ApplicationException {
        if (QueryTypes.QUERY_SEARCH.toString().equals(queryId)) {
            String userId = ctx.getUserId();
            String user_type = ctx.getUserType();
            String resolution = (String) map.get("resolution");
            String query_text = (String) map.get("query_text");
            String pageno_str = (String) map.get("pageno");
            int pageno = 0;
            if (Util.isEmpty(resolution)) {
                resolution = "hdpi";
            }
            if (!Util.isEmpty(pageno_str)) {
                pageno = Integer.parseInt(pageno_str);
            }
            BaseResource[] resources = null;

            if ("USER".equalsIgnoreCase(user_type)) {
                Expression e1 = new Expression(search_data.FIELD_SHOW_USER, REL_OP.EQ,"Y");

                Expression e2 = new Expression(search_data.FIELD_TITLE, REL_OP.REGEX,query_text);
                Expression e3 = new Expression(search_data.FIELD_CONTENT_TEXT, REL_OP.REGEX,query_text);

                Expression e4 = new Expression(search_data.FIELD_LOCATION_NAME, REL_OP.REGEX,query_text);
                Expression e5 = new Expression(search_data.FIELD_NARRATED_BY_NAME, REL_OP.REGEX,query_text);

                Expression e6 = new Expression(search_data.FIELD_KEYWORDS, REL_OP.REGEX,query_text);
                Expression e7 = new Expression(search_data.FIELD_QUALITIES, REL_OP.REGEX,query_text);

                Expression e8 = new Expression(e2 , LOG_OP.OR, e3);
                Expression e9 = new Expression(e4 , LOG_OP.OR, e5);
                Expression e10 = new Expression(e6, LOG_OP.OR, e7);

                Expression e11 = new Expression(e8, LOG_OP.OR, e9);

                Expression e12 = new Expression(e10, LOG_OP.OR, e11);

                Expression e = new Expression(e1, LOG_OP.AND, e12);

                resources = Search_dataHelper.getInstance().getPage(e,null,pageno,50);

                BaseResource[] _res = resources;
                if (_res.length<=0) {
                    throw new ApplicationException(ExceptionSeverity.INFO,"Nothing found with the query "+"\""+query_text+"\"");
                }
                for (BaseResource resource : _res) {
                    search_data _search = (search_data) resource;
                    _search.setOrder(999999);
                    Search_dataHelper.getInstance().update(_search);
                    String content_text = _search.getContent_text();
                    String content_text_str = content_text.replaceAll("\\<.*?>", "");
                    int found_pos = content_text_str.indexOf(query_text);
                    int text_length = content_text_str.length();
                    String next_str = "", prev_str = "", cont_str = "";
                    if (found_pos>-1) {
                        if ((text_length - found_pos) >= 60) {
                            try {
                                next_str = (content_text_str.substring(found_pos+query_text.length(), found_pos+query_text.length()+60)).trim();
                            } catch (Exception ex) {

                            }

                        }
                        if (found_pos>60) {
                            prev_str = (content_text_str.substring(found_pos - 60, found_pos + query_text.length())).trim();
                            cont_str = prev_str + " " + next_str;
                            int space_index = cont_str.indexOf(" ");
                            cont_str = cont_str.substring(space_index, cont_str.length()).trim();
                            _search.setContent_text(cont_str);
                        }
                    }

                    // Ranking algorithm
                    if ((_search.getTitle().toLowerCase().contains(query_text)) && (_search.getContent_text().toLowerCase().contains(query_text)) && ((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text))) {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(1);
                        Search_dataHelper.getInstance().update(_search);
                    } else if ((((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text)) && (_search.getTitle().toLowerCase().contains(query_text)))
                            || (((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text)) && (query_text.contains(_search.getContent_text().toLowerCase())))) {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(2);
                        Search_dataHelper.getInstance().update(_search);
                    } else if ((query_text.contains(_search.getTitle().toLowerCase())) && !((((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text)) && (query_text.contains(_search.getTitle().toLowerCase()))))) {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(3);
                        Search_dataHelper.getInstance().update(_search);
                    } else if (((query_text.contains(_search.getContent_text().toLowerCase())) && !((((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text)) && (query_text.contains(_search.getTitle().toLowerCase())))))) {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(4);
                        Search_dataHelper.getInstance().update(_search);
                    } else {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(5);
                        Search_dataHelper.getInstance().update(_search);
                    }
                }

                List<BaseResource> _list = HelperUtils.convertArray2List(_res);
                _list.sort(new Comparator<BaseResource>() {
                    @Override
                    public int compare(BaseResource obj1, BaseResource obj2) {
                        return Long.compare(((search_data) obj1).getOrder(), ((search_data) obj2).getOrder());
                    }
                });

                BaseResource[] ordered_res = _list.toArray(new search_data[_list.size()]);

                //Add in Search Log
                Search_LogHelper.getInstance().addSearchLog(userId, query_text);

                return ordered_res;
            } else if ("PREACHER".equalsIgnoreCase(user_type)) {
                Expression e1 = new Expression(search_data.FIELD_SHOW_PREACHER, REL_OP.EQ,"Y");
                Expression e2 = new Expression(search_data.FIELD_TITLE, REL_OP.REGEX,query_text);
                Expression e3 = new Expression(search_data.FIELD_CONTENT_TEXT, REL_OP.REGEX,query_text);

                Expression e4 = new Expression(search_data.FIELD_LOCATION_NAME, REL_OP.REGEX,query_text);
                Expression e5 = new Expression(search_data.FIELD_NARRATED_BY_NAME, REL_OP.REGEX,query_text);

                Expression e6 = new Expression(search_data.FIELD_KEYWORDS, REL_OP.REGEX,query_text);
                Expression e7 = new Expression(search_data.FIELD_QUALITIES, REL_OP.REGEX,query_text);

                Expression e8 = new Expression(e2 , LOG_OP.OR, e3);
                Expression e9 = new Expression(e4 , LOG_OP.OR, e5);
                Expression e10 = new Expression(e6, LOG_OP.OR, e7);

                Expression e11 = new Expression(e8, LOG_OP.OR, e9);

                Expression e12 = new Expression(e10, LOG_OP.OR, e11);

                Expression e = new Expression(e1, LOG_OP.AND, e12);
                resources =Search_dataHelper.getInstance().getPage(e,null,pageno,50);

                BaseResource[] _res = resources;
                if (_res.length<=0) {
                    throw new ApplicationException(ExceptionSeverity.INFO,"Nothing found with the query "+"\""+query_text+"\"");
                }
                for (BaseResource resource : _res) {
                    search_data _search = (search_data) resource;
                    String content_text = _search.getContent_text();
                    String content_text_str = content_text.replaceAll("\\<.*?>", "");
                    int found_pos = content_text_str.indexOf(query_text);
                    int text_length = content_text_str.length();
                    String next_str = "", prev_str = "", cont_str = "";
                    if (found_pos > -1) {
                        if ((text_length - found_pos) > 60) {
                            next_str = (content_text_str.substring(found_pos + query_text.length(), found_pos + query_text.length() + 60)).trim();
                        }
                        if (found_pos > 60) {
                            prev_str = (content_text_str.substring(found_pos - 60, found_pos + query_text.length())).trim();
                            cont_str = prev_str + " " + next_str;
                            int space_index = cont_str.indexOf(" ");
                            cont_str = cont_str.substring(space_index, cont_str.length()).trim();
                            _search.setContent_text(cont_str);
                        }
                    }

                    // Ranking algorithm - Not working properly
                    if ((_search.getTitle().toLowerCase().contains(query_text)) && (_search.getContent_text().toLowerCase().contains(query_text)) && ((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text))) {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(1);
                        Search_dataHelper.getInstance().update(_search);
                    } else if ((((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text)) && (_search.getTitle().toLowerCase().contains(query_text)))
                            || (((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text)) && (query_text.contains(_search.getContent_text().toLowerCase())))) {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(2);
                        Search_dataHelper.getInstance().update(_search);
                    } else if ((query_text.contains(_search.getTitle().toLowerCase())) && !((((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text)) && (query_text.contains(_search.getTitle().toLowerCase()))))) {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(3);
                        Search_dataHelper.getInstance().update(_search);
                    } else if (((query_text.contains(_search.getContent_text().toLowerCase())) && !((((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text)) && (query_text.contains(_search.getTitle().toLowerCase())))))) {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(4);
                        Search_dataHelper.getInstance().update(_search);
                    } else {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(5);
                        Search_dataHelper.getInstance().update(_search);
                    }
                }

                List<BaseResource> _list = HelperUtils.convertArray2List(_res);
                _list.sort(new Comparator<BaseResource>() {
                    @Override
                    public int compare(BaseResource obj1, BaseResource obj2) {
                        return Long.compare(((search_data) obj1).getOrder(), ((search_data) obj2).getOrder());
                    }
                });

                BaseResource[] ordered_res = _list.toArray(new search_data[_list.size()]);

                //Add in Search Log
                Search_LogHelper.getInstance().addSearchLog(userId, query_text);

                return ordered_res;
            } else {
                Expression e2 = new Expression(search_data.FIELD_TITLE, REL_OP.REGEX,query_text);
                Expression e3 = new Expression(search_data.FIELD_CONTENT_TEXT, REL_OP.REGEX,query_text);

                Expression e4 = new Expression(search_data.FIELD_LOCATION_NAME, REL_OP.REGEX,query_text);
                Expression e5 = new Expression(search_data.FIELD_NARRATED_BY_NAME, REL_OP.REGEX,query_text);

                Expression e6 = new Expression(search_data.FIELD_KEYWORDS, REL_OP.REGEX,query_text);
                Expression e7 = new Expression(search_data.FIELD_QUALITIES, REL_OP.REGEX,query_text);

                Expression e8 = new Expression(e2 , LOG_OP.OR, e3);
                Expression e9 = new Expression(e4 , LOG_OP.OR, e5);
                Expression e10 = new Expression(e6, LOG_OP.OR, e7);

                Expression e11 = new Expression(e8, LOG_OP.OR, e9);
                Expression e = new Expression(e10, LOG_OP.OR, e11);
                resources = Search_dataHelper.getInstance().getPage(e,null,pageno,50);

                BaseResource[] _res = resources;
                if (_res.length<=0) {
                    throw new ApplicationException(ExceptionSeverity.INFO,"Nothing found with the query "+"\""+query_text+"\"");
                }
                for (BaseResource resource : _res) {
                    search_data _search = (search_data) resource;
                    String content_text = _search.getContent_text();
                    String content_text_str = content_text.replaceAll("\\<.*?>", "");
                    int found_pos = content_text_str.indexOf(query_text);
                    int text_length = content_text_str.length();
                    String next_str = "", prev_str = "", cont_str = "";
                    if (found_pos > -1) {
                        if ((text_length - found_pos) > 60) {
                            next_str = (content_text_str.substring(found_pos + query_text.length(), found_pos + query_text.length() + 60)).trim();
                        }
                        if (found_pos > 60) {
                            prev_str = (content_text_str.substring(found_pos - 60, found_pos + query_text.length())).trim();
                            cont_str = prev_str + " " + next_str;
                            int space_index = cont_str.indexOf(" ");
                            cont_str = cont_str.substring(space_index, cont_str.length()).trim();
                            _search.setContent_text(cont_str);
                        }
                    }
                    // Ranking algorithm
                    if ((_search.getTitle().toLowerCase().contains(query_text)) && (_search.getContent_text().toLowerCase().contains(query_text)) && ((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text))) {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(1);
                        Search_dataHelper.getInstance().update(_search);
                    } else if ((((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text)) && (_search.getTitle().toLowerCase().contains(query_text)))
                            || (((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text)) && (query_text.contains(_search.getContent_text().toLowerCase())))) {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(2);
                        Search_dataHelper.getInstance().update(_search);
                    } else if ((query_text.contains(_search.getTitle().toLowerCase())) && !((((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text)) && (query_text.contains(_search.getTitle().toLowerCase()))))) {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(3);
                        Search_dataHelper.getInstance().update(_search);
                    } else if (((query_text.contains(_search.getContent_text().toLowerCase())) && !((((_search.getQualities() + "").toLowerCase().contains(query_text) || (_search.getKeywords() + "").toLowerCase().contains(query_text) || (_search.getLocation_name() + "").toLowerCase().contains(query_text) || (_search.getNarrated_by_name() + "").toLowerCase().contains(query_text)) && (query_text.contains(_search.getTitle().toLowerCase())))))) {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(4);
                        Search_dataHelper.getInstance().update(_search);
                    } else {
                        Search_dataHelper.getInstance().getById(_search.getId());
                        _search.setOrder(5);
                        Search_dataHelper.getInstance().update(_search);
                    }
                }

                List<BaseResource> _list = HelperUtils.convertArray2List(_res);
                _list.sort(new Comparator<BaseResource>() {
                    @Override
                    public int compare(BaseResource obj1, BaseResource obj2) {
                        return Long.compare(((search_data) obj1).getOrder(), ((search_data) obj2).getOrder());
                    }
                });

                BaseResource[] ordered_res = _list.toArray(new search_data[_list.size()]);

                //Add in Search Log
                Search_LogHelper.getInstance().addSearchLog(userId, query_text);

                return ordered_res;
            }
        }
        throw new ApplicationException(ExceptionSeverity.ERROR, "Invalid Query");
    }
}
