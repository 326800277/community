package weididi.community.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import weididi.community.community.exception.CustomizeErrorCode;
import weididi.community.community.exception.CustomizeException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class UcloudProvider {
    @Value("${ucloud.ufile.public-key}")
    private String publicKey;
    @Value("${ucloud.ufile.private-key}")
    private String privateKey;
    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName;
    @Value("${ucloud.ufile.expiresDuration}")
    private Integer expiresDuration;

    //实现同步上传，这个地方因为是在项目内实现的，只能上传项目内的文件
    public String upload(InputStream fileStream,String mimeType,String fileName){
        //这两行不能放在外面，会导致spring无法自动注入
        //授权器
        ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey, privateKey);
        // 对象操作需要ObjectConfig来配置您的地区和域名后缀
        ObjectConfig config = new ObjectConfig("cn-bj", "ufileos.com");

        String generatedFileName;
        //分成前后两部分，这个地方.是正则表达式，需要转义
        String[] filePaths=fileName.split("\\.");
        if(filePaths.length>1){
            //获得上传的格式并添加随机码
            generatedFileName=UUID.randomUUID().toString()+"."+filePaths[filePaths.length-1];
        }else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        try {
            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)
                    /**
                     * 是否上传校验MD5, Default = true
                     */
                    //  .withVerifyMd5(false)
                    /**
                     * 指定progress callback的间隔, Default = 每秒回调
                     */
                    //  .withProgressConfig(ProgressConfig.callbackWithPercent(10))
                    /**
                     * 配置进度监听
                     */
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(long bytesWritten, long contentLength) {
                        }
                    })
                    .execute();

                    if(response!=null&&response.getRetCode()==0){
                        String url=UfileClient.object(objectAuthorization,config)
                                .getDownloadUrlFromPrivateBucket(generatedFileName,bucketName,expiresDuration)
                                .createUrl();
                        return url;
                    }else {
                        throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
                    }
        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
    }

}
