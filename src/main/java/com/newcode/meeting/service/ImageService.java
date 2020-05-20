package com.newcode.meeting.service;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.newcode.meeting.config.FireBaseConfig;
import com.newcode.meeting.domain.Image;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.repo.ImageRepo;
import com.newcode.meeting.repo.UserRepo;
import com.newcode.meeting.util.RandomHelper;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ImageService {
    private final FireBaseConfig fireBase;
    private final ImageRepo imageRepo;
    private final UserRepo userRepo;

    public ImageService(FireBaseConfig fireBase, ImageRepo imageRepo, UserRepo userRepo) {
        this.fireBase = fireBase;
        this.imageRepo = imageRepo;
        this.userRepo = userRepo;
    }

    public Image addImage(Image newImage, User user) throws IOException {
        Set<Image> images = imageRepo.findImageByUserId(user.getId());
        user = userRepo.findUserById(user.getId());
        if (images.size() > 4) {
            return new Image(null);
        }

        File outPutFile = new File("image.jpg");
        InputStream testFile = new FileInputStream("./image.jpg");
        String name = RandomHelper.generatePassword(6);
        String blobName = user.getId() + "/" + name + ".jpg";
        String bucketName = "meeting-app-af0af.appspot.com";
        String keyPath = "./serviceAccountKey.json";

        String imageDataBytes = newImage.getName().substring(newImage.getName().indexOf(",") + 1);
        System.out.println(imageDataBytes.length());
        byte[] decodedImageByte = Base64.getDecoder().decode(imageDataBytes);
        BufferedImage bufferedImage = getBufferedImage(decodedImageByte);
        ImageIO.write(bufferedImage, "jpg", outPutFile);

        Blob blob = fireBase.getBucket().create(
                blobName, testFile, "image/jpg",
                Bucket.BlobWriteOption.userProject("meeting-app-af0af")
        );
        URL signedUrl = blob.getStorage().signUrl(BlobInfo.newBuilder(bucketName, blobName).build(),
                10000, TimeUnit.DAYS, Storage.SignUrlOption.signWith(ServiceAccountCredentials.fromStream(
                        new FileInputStream(keyPath))));
        Image image = new Image();
        image.setName(name);
        image.setUrlLink(signedUrl);
        image.setUser(user);
        if (images.size() == 0) {
            image.setMain(true);
            user.setUserpic(signedUrl);
        }
        userRepo.save(user);
        imageRepo.save(image);

        return image;
    }


    private BufferedImage getBufferedImage(byte[] decodedImageByte) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(decodedImageByte));
        BufferedImage newBufferedImage = new BufferedImage(
                bufferedImage.getWidth(),
                bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
        return newBufferedImage;
    }

    public void remove(Image image, User user) {

        String blobName = user.getId() + "/" + image.getName() + ".jpg";
        String bucketName = "meeting-app-af0af.appspot.com";

        imageRepo.delete(image);

        boolean delete = fireBase.getBucket().getStorage().delete(
                bucketName, blobName, Storage.BlobSourceOption.userProject("meeting-app-af0af"));
        if (!delete){
            imageRepo.save(image);
        }
    }

    public Image editImageMainAndHide(Image imageFromDb, Image image, User userPrincipal) {

        if (image.isMain()){
            userPrincipal.setUserpic(imageFromDb.getUrlLink());
            userRepo.save(userPrincipal);

            Image imageOldMain = imageRepo.findByMainAndUser_Id(true, userPrincipal.getId());
            imageOldMain.setMain(false);
            imageFromDb.setMain(image.isMain());
            imageFromDb.setHide(image.isHide());
            List<Image> images = new ArrayList<>();
            images.add(imageFromDb);
            images.add(imageOldMain);
            imageRepo.saveAll(images);

        }else {
            imageFromDb.setHide(image.isHide());
            imageRepo.save(imageFromDb);
        }
        return imageFromDb;
    }
}
