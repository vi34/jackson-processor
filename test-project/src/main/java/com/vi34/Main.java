package com.vi34;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.google.common.io.Files;
import com.vi34.deserializers.MediaItemDeserializer;
import com.vi34.deserializers.MediaItemDeserializerHand;
import com.vi34.entities.Pojo;
import com.vi34.entities.Complex;
import com.vi34.entities.PrivatePojo;
import com.vi34.entities.media.MediaItem;
import com.vi34.serializers.PojoSerializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by vi34 on 13/08/16.
 */

public class Main {

    private static File testFile = new File("test.json");

    public static void main(String[] args) throws IOException {
        BufferedWriter writer = Files.newWriter(testFile, Charset.defaultCharset());
        MappingJsonFactory factory = new MappingJsonFactory();
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        SimpleModule module = new SimpleModule();
//        module.addSerializer(Pojo.class, new PojoSerializer());
        module.addSerializer(MediaItem.class, new com.vi34.serializers.MediaItemSerializer());
        module.addDeserializer(MediaItem.class, new MediaItemDeserializer());
        mapper.registerModule(module);
        //mapper.registerModule(new AfterburnerModule());

        //Pojo pojo = new Pojo(1, "test", Arrays.asList(3, 4 ,5 ,1), false, 3.1, 999, 'a');
        //MediaItem value = MediaItem.buildItem();
       // mapper.writeValue(writer, value);
        MediaItem read = mapper.readValue(new File("deser.json"), MediaItem.class);
        System.out.println(read);
        /*PrivatePojo pojo = PrivatePojo.makePrivatePojo();
        mapper.writeValue(System.out, pojo);

        mapper.writeValue(writer, pojo);

        PrivatePojo pojo2 = mapper.readValue(Files.newReader(testFile, Charset.defaultCharset()), PrivatePojo.class);
        System.out.println(pojo2);*/

    }
}
