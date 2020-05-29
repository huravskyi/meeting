package com.newcode.meeting.service;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.newcode.meeting.config.FireBaseConfig;
import com.newcode.meeting.domain.Image;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.repo.ImageRepo;
import com.newcode.meeting.repo.UserRepo;
import com.newcode.meeting.util.RandomHelper;
import org.springframework.stereotype.Service;
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
    private Image writeToFireBase(User user, Image newImage, Set<Image> images) throws IOException {
        String imageDataBytes = newImage.getName().substring(newImage.getName().indexOf(",") + 1);
        byte[] decodedImageByte = new byte[0];
        try{
            decodedImageByte = Base64.getDecoder().decode(imageDataBytes);
        }catch (Exception e){
            e.fillInStackTrace();
        }

        String name = RandomHelper.generatePassword(6);
        String blobName = user.getId() + "/" + name + ".jpg";
        String bucketName = "meeting-app-af0af.appspot.com";
        String keyPath = "./serviceAccountKey.json";
        Blob blob = fireBase.getBucket().create(
                blobName,
                decodedImageByte,
                "image/jpg");
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
        return image;
    }

    public Image addImage(Image newImage, User user) {
        Set<Image> images = imageRepo.findImageByUserId(user.getId());
        user = userRepo.findUserById(user.getId());
        if (images.size() > 4) {
            return new Image(null);
        }

        Image image = null;
        try {
            image = writeToFireBase(user, newImage, images);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (image != null) {
            userRepo.save(user);
            imageRepo.save(image);
        }
        return image;
    }

    public void remove(Image image, User user) {

        String blobName = user.getId() + "/" + image.getName() + ".jpg";
        String bucketName = "meeting-app-af0af.appspot.com";

        imageRepo.delete(image);

        boolean delete = fireBase.getBucket().getStorage().delete(
                bucketName, blobName, Storage.BlobSourceOption.userProject("meeting-app-af0af"));
        if (!delete) {
            imageRepo.save(image);
        }
    }

    public Image editImageMainAndHide(Image imageFromDb, Image image, User userPrincipal) {

        if (image.isMain()) {
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

        } else {
            imageFromDb.setHide(image.isHide());
            imageRepo.save(imageFromDb);
        }
        return imageFromDb;
    }
}
