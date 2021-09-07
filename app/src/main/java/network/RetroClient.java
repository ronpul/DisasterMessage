package network;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.CustomLogger;
import utils.Utils;

public class RetroClient {
    public static final String SERVER_ADDRESS = "http://146.56.161.101/api/";
    private static Object retrofitService;

    public static ApiInterface getApiInterface(){
        return (ApiInterface) getClient();
    }

    private static Object getClient() {
        if (retrofitService == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

            httpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    if (Utils.isNetworkConnected() == false) {
                        throw new NetworkNotConnectedException();
                    }

                    return chain.proceed(chain.request());
                }
            });

            httpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
            httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);

            if (CustomLogger.IS_DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClientBuilder.addInterceptor(logging);
            }

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory());
            gsonBuilder.serializeNulls();

           Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_ADDRESS)
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(httpClientBuilder.build())
                    .build();

            retrofitService = retrofit.create(ApiInterface.class);
        }
        return retrofitService;
    }

    private static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    private static class StringAdapter extends TypeAdapter<String> {

        @Override
        public void write(JsonWriter writer, String value) throws IOException {
            if (TextUtils.isEmpty(value)) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }

        @Override
        public String read(com.google.gson.stream.JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return "";
            }
            return in.nextString();
        }
    }

    private static String localeToBcp47Language(Locale loc) {
        try {
            final char SEP = '-';       // we will use a dash as per BCP 47
            String language = loc.getLanguage();
            String region = loc.getCountry();
            String variant = loc.getVariant();

            // special case for Norwegian Nynorsk since "NY" cannot be a variant as per BCP 47
            // this goes before the string matching since "NY" wont pass the variant checks
            if (language.equals("no") && region.equals("NO") && variant.equals("NY")) {
                language = "nn";
                region = "NO";
                variant = "";
            }

            if (language.isEmpty() || !language.matches("\\p{Alpha}{2,8}")) {
                language = "und";       // Follow the Locale#toLanguageTag() implementation
                // which says to return "und" for Undetermined
            } else if (language.equals("iw")) {
                language = "he";        // correct deprecated "Hebrew"
            } else if (language.equals("in")) {
                language = "id";        // correct deprecated "Indonesian"
            } else if (language.equals("ji")) {
                language = "yi";        // correct deprecated "Yiddish"
            }

            // ensure valid country code, if not well formed, it's omitted
            if (!region.matches("\\p{Alpha}{2}|\\p{Digit}{3}")) {
                region = "";
            }

            // variant subtags that begin with a letter must be at least 5 characters long
            if (!variant.matches("\\p{Alnum}{5,8}|\\p{Digit}\\p{Alnum}{3}")) {
                variant = "";
            }

            StringBuilder bcp47Tag = new StringBuilder(language);
            if (!region.isEmpty()) {
                bcp47Tag.append(SEP).append(region);
            }
            if (!variant.isEmpty()) {
                bcp47Tag.append(SEP).append(variant);
            }

            return bcp47Tag.toString();
        } catch (Exception e) {
        }

        return "ko-KR";
    }
}


