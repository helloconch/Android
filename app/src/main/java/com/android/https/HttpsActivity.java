package com.android.https;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.testing.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheyanxu on 16/7/26.
 */
public class HttpsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_https);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.testHttps)
    public void onClick() {
        new Thread(new MyRunnable3()).start();
    }


    /**
     * 访问的百度
     * 数字证书是由全球知名的CA签发
     */
    private static class MyRunnable1 implements Runnable {
        @Override
        public void run() {
            try {
                URL url = new URL("https://www.baidu.com");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String s;
                StringBuilder stringBuilder = new StringBuilder();
                while ((s = br.readLine()) != null) {
                    stringBuilder.append(s);
                }
                br.close();

                Log.i("MMMMMMM", stringBuilder.toString());


            } catch (Exception e) {

                e.printStackTrace();

            }

        }
    }

    /**
     * 访问12306
     * 12306数字签名是由SRCA
     * SRCA是铁道部给旗下的网站等做签名的一个所谓CA，但是它不具备公信力，因为它不是一个全球知名的CA，所以客户端根本不认可它
     * <p/>
     * <p/>
     * 我们在Android中直接访问https://kyfw.12306.cn/otn/regist/init时，会得到如下异常：
     * Java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.
     * 这是因为Android的客户端内置的可信任CA列表中没有包含所谓的SRCA，这样就出现了12306.cn的证书不被客户端信任的异常
     */
    private static final class MyRunnable2 implements Runnable {
        @Override
        public void run() {
            try {
                URL url = new URL("https://kyfw.12306.cn/otn/regist/init");
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                InputStream is = httpsURLConnection.getInputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * 解决12306证书问题
     * 们将12306网站自身的12306.cer放到了assets目录中，然后让我们创建的HttpsURLConnection的实例信任了12306.cer。但是，数字证书都是有过期时间的，
     * 如果12306网站的数字证书到期了，那么12306会去SRCA那里重新生成一个数字证书，这时候12306网站的公钥和私钥都会更新，那这样就存在问题了。
     * 我们App的assets目录中存储的是老的12306.cer证书，这样12306网站重新生成了新的数字证书，那么老的数字证书就自动作废了，
     * 因为我们App中的12306.cer中的老的公钥无法解密12306网站最新的私钥了（公钥和私钥只能成对出现，旧的公钥只能解密旧的私钥）。
     * <p/>
     * 很不幸的是，网上大部分的解决方案就是直接信任12306.cer这种网站自身的数字证书，虽然这种办法暂时可以解决HTTPS问题，但是不是长久之计，会为以后的数字证书的更新埋下隐患。
     * <p/>
     * 那怎么解决这个问题呢？
     * <p/>
     * 最好的办法不是让我们的App直接信任12306.cer，而是让我们的App直接信任12306数字证书的签发者SRCA的数字证书。
     */
    private final class MyRunnable3 implements Runnable {
        @Override
        public void run() {

            HttpsURLConnection conn = null;
            InputStream is = null;
            try {
                URL url = new URL("https://kyfw.12306.cn/otn/regist/init");
                conn = (HttpsURLConnection) url.openConnection();
                //创建X.509格式的CertificateFactory
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                //从assets中获取证书的流
                InputStream cerInputStream = getAssets().open("srca.cer");
                Certificate ca;
                try {
                    //证书工厂根据证书文件的流生成证书Certificate
                    ca = cf.generateCertificate(cerInputStream);
                    Log.i("MMMMM", "ca=" + ((X509Certificate) ca).getSubjectDN());
                } finally {
                    if (cerInputStream != null)
                        cerInputStream.close();
                }

                //创建一个默认类型的KeyStore，存储我们信任的证书
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                //将证书ca作为信任的证书放入到keyStore中
                keyStore.setCertificateEntry("ca", ca);
                //TrustManagerFactory是用于生成TrustManager的，我们创建一个默认类型的TrustManagerFactory
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                //用我们之前的keyStore实例初始化TrustManagerFactory，这样tmf就会信任keyStore中的证书
                tmf.init(keyStore);
                //通过tmf获取TrustManager数组，TrustManager也会信任keyStore中的证书
                TrustManager[] trustManagers = tmf.getTrustManagers();
                //创建TLS类型的SSLContext对象， that uses our TrustManager
                SSLContext sslContext = SSLContext.getInstance("TLS");
                //用上面得到的trustManagers初始化SSLContext，这样sslContext就会信任keyStore中的证书
                sslContext.init(null, trustManagers, null);
                //通过sslContext获取SSLSocketFactory对象
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                //将sslSocketFactory通过setSSLSocketFactory方法作用于HttpsURLConnection对象
                //这样conn对象就会信任我们之前得到的证书对象
                conn.setSSLSocketFactory(sslSocketFactory);


                is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String s;
                final StringBuilder stringBuilder = new StringBuilder();
                while ((s = br.readLine()) != null) {
                    stringBuilder.append(s);
                }
                br.close();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }


        }
    }
}
