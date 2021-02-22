package com.example.bimapp.managers;

import android.os.Environment;
import android.util.Log;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.example.bimapp.Bimapp;
import com.example.bimapp.interfaces.IDownloadCallback;
import com.example.bimapp.screens.modes.energy.Energy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static android.os.Environment.DIRECTORY_MUSIC;

public class DropBoxManager {
    private static DropBoxManager sDropBoxManager;
    private DbxClientV2 mClient;

    public ArrayList<Metadata> mDownloadedFilesList = new ArrayList<>();

    public static DropBoxManager getInstance() {
        if (sDropBoxManager == null) {
            sDropBoxManager = new DropBoxManager();
        }
        return sDropBoxManager;
    }
     // Johan      kjV7ECrsWt8AAAAAAAAAAYpkFOyV4TE-IoemAK1nP3_sz-ApV0iVdTqatQ7YdRyG
    //hammad       y_Zm2Eq9pbIAAAAAAAAAAVVxSTS3uumpX29Eh2B_BjWAh_NsuP5r46Wg3Nu1asIv   OEM6OTsy5N8AAAAAAAAAARR5JGEDDx66_389ymBRxgC4I-PibaGIEEnezB_f4QmM
    //madeleine    fa30d68ebead77ccdc74c5acb57c3b873553599d    CNPqZMLPnOIAAAAAAAAAAWVkIap0nk4FEvtbOB2w7ula7TFAco-qdyOdV9WHx_DZ
    public void connect() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        mClient = new DbxClientV2(config, "t299wd3F9ygAAAAAAAAAAdXr8Lg4PuWt3s9Xp9NSpiZMGTD9h0eelqzpIhucAP0j");
        // Get current account info

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FullAccount account = null;
                try {
                    account = mClient.users().getCurrentAccount();
                } catch (DbxException e) {
                    e.printStackTrace();
                }
                System.out.println("Account name: "+account.getName().getDisplayName());
            }
        });
        thread.start();

    }


    public void startDownload(final int index,final String FolderPath, final IDownloadCallback iDownloadCallback)  {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        downloadFiles(index,FolderPath,iDownloadCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DbxException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();



    }

    public void downloadFiles(int index,String FolderPath,final IDownloadCallback iDownloadCallback) throws IOException, DbxException {

        ListFolderResult result = mClient.files().listFolder("/SampleFolder/"+FolderPath);
                /*     /sample audio bim/            /data/All/     */
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println("path lower" + metadata.getPathLower());

            }

                    if (!result.getHasMore()) {
                        break;
                    }

            result = mClient.files().listFolderContinue(result.getCursor());
        }



        Log.e("Total number of files",""+result.getEntries().size());


        Metadata metadata = result.getEntries().get(index);
        Log.e("Downloaded file path",metadata.getPathLower());
        DbxDownloader<FileMetadata> downloader = mClient.files().download(metadata.getPathLower());

        try {

            String filePath = getFolderPath() + "/" + metadata.getName();
            File f = new File(filePath);

            if (!f.exists()) {

                //f.delete();
                f.createNewFile();
                f.setWritable(true);
                FileOutputStream out = new FileOutputStream(f);
                downloader.download(out);
                out.close();
            }


            Metadata downloadedFileMeta = new Metadata(metadata.getName(), filePath, null, null);
            mDownloadedFilesList.add(downloadedFileMeta);
        } catch (DbxException ex) {
            System.out.println(ex.getMessage());
        }
                if (iDownloadCallback != null) {
            iDownloadCallback.onComplete();
        }


    }
    public boolean isFileExist(String  filename) {

        String filePath = DropBoxManager.getInstance().getFolderPath() + "/" + filename;
        File f = new File(filePath);
        if (f.exists()) {
            return true;
        } else {
            return false;
        }
    }



    public  String getFolderPath() {
        String path = Bimapp.getGenericContext().getExternalFilesDir(DIRECTORY_MUSIC).getPath();
        File file = new File(path, "MusicFolder");
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public boolean isDownloadedFilesListEmpty() {

        return mDownloadedFilesList == null || mDownloadedFilesList.isEmpty();

            }
}
