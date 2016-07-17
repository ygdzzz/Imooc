package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ExampleDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(4, "com.example.ygd.greendao");
        addVedioDownload(schema);
        addVedio(schema);
        addClassProj(schema);
        addBlog(schema);
        new DaoGenerator().generateAll(schema, "D:/AndroidStudioWorkspace/Imooc/app/src/main/java-gen");
    }

    /**
     * @param schema
     */
    private static void addVedioDownload(Schema schema) {
        Entity entity = schema.addEntity("VedioDownload");
        entity.setTableName("vediodownload");
        entity.addIdProperty().primaryKey();
        entity.addStringProperty("vedioid");
        entity.addStringProperty("vedioName");
        entity.addStringProperty("VUri");
        entity.addStringProperty("projId");
        entity.addStringProperty("instruction");
        entity.addStringProperty("author");
        entity.addStringProperty("pubDate");
        entity.addStringProperty("VPickUri");
        entity.addStringProperty("flag");
    }


    private static void addVedio(Schema schema) {
        Entity entity = schema.addEntity("Vedio");
        entity.setTableName("vedio");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("vedioid");
        entity.addStringProperty("vedioName");
        entity.addStringProperty("VUri");
        entity.addStringProperty("projId");
        entity.addStringProperty("instruction");
        entity.addStringProperty("author");
        entity.addStringProperty("pubDate");
        entity.addStringProperty("VPickUri");
        entity.addStringProperty("flag");
    }

    private static void addClassProj(Schema schema) {
        Entity entity = schema.addEntity("ClassProj");
        entity.setTableName("classproj");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("endDate");
        entity.addStringProperty("photoUri");
        entity.addStringProperty("projId");
        entity.addStringProperty("remark");
        entity.addStringProperty("rgdtDate");
        entity.addStringProperty("vedioCt");
    }

    private static void addBlog(Schema schema) {
        Entity entity = schema.addEntity("Blog");
        entity.setTableName("blog");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("comment");
        entity.addStringProperty("date");
        entity.addStringProperty("read");
        entity.addStringProperty("title");
        entity.addStringProperty("type");
        entity.addStringProperty("url");
    }
}
