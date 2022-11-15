package ru.clevertec.ecl.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

  public static final String FIELD_NAME_ID = "id";
  public static final String FIELD_NAME_NAME = "name";
  public static final String FIELD_NAME_DESCRIPTION = "description";

  public static final String TABLE_CERTIFICATE_NAME = "gift_certificate";

  public static final String CERTIFICATE = "Certificate";
  public static final String TAG = "Tag";
  public static final String ORDER = "Order";
  public static final String USER = "User";

  public static final String REDIRECT = "redirect";
  public static final String REPLICATE = "replicate";
  public static final String PAGE = "page";
  public static final String SIZE = "size";
  public static final String LIMIT_ZERO = "limit=0";

  public static final String ENTITY_GRAPH_NAME_CERTIFICATE_TAG = "certificateTag";
  public static final String ENTITY_ATTRIBUTE_TAG = "tags";

  public static final String SCHEME_HTTP = "http";

  public static final String URL_HEALTH = "/api/actuator/health";
  public static final String URL_SEQUENCE = "/api/commitlog/current";
  public static final String URL_RECOVERY_DATA = "/api/commitlog/recovery/{limit}}";
  public static final String REQUEST_NEXT_SEQUENCE = "/api/v1/orders/sequence/set";
  public static final String REQUEST_LAST_VALUE_SEQUENCE = "/api/v1/orders/sequence/current";
  public static final String URL_OFFSET_LIMIT_REQUEST = "http://node%s:%d/api/v1/orders/offset?limit=%d&offset=%d";
}
