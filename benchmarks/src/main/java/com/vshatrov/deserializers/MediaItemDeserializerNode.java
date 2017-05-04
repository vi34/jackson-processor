package com.vshatrov.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.vshatrov.entities.media.*;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Viktor Shatrov.
 */
public class MediaItemDeserializerNode extends JsonDeserializer<MediaItem> {
    @Override
    public MediaItem deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec codec = p.getCodec();
        JsonNode treeNode = codec.readTree(p);
        MediaItem mediaItem = new MediaItem();
        mediaItem.media = readMedia(treeNode.get("media"));
        mediaItem.images = new ArrayList<>();
        ((ArrayNode)treeNode.get("images")).elements()
                .forEachRemaining(jsonNode -> mediaItem.images.add(readImage(jsonNode)));
        return mediaItem;
    }

    public Media readMedia(JsonNode node) throws IOException {
        Media media = new Media();
        JsonNode uriNode = node.get("uri");
        media.uri = uriNode == null ? null : uriNode.asText();
        JsonNode titleNode = node.get("title");
        media.title = titleNode == null ? null : titleNode.asText();
        JsonNode formatNode = node.get("format");
        media.format = formatNode == null ? null : formatNode.asText();
        JsonNode copyNode = node.get("copyright");
        media.copyright = copyNode == null ? null : copyNode.asText();
        IntNode wNode = (IntNode)node.get("width");
        media.width = wNode == null ? 0 : (Integer) wNode.numberValue();
        IntNode hNode = (IntNode)node.get("height");
        media.height = hNode == null ? 0 : hNode.numberValue().intValue();
        NumericNode durNode = (NumericNode) node.get("duration");
        media.duration = durNode == null ? 0 : durNode.numberValue().longValue();
        NumericNode sizeNode = (NumericNode) node.get("size");
        media.size = sizeNode == null ? 0 :  sizeNode.numberValue().longValue();
        IntNode bitNode = (IntNode)node.get("bitrate");
        media.bitrate = bitNode == null ? 0 : (Integer) bitNode.numberValue();
        //BooleanNode hasBitNode = (BooleanNode) node.get("hasBitrate");
        //media.hasBitrate = hasBitNode != null && hasBitNode.asBoolean();
        media.player = readPlayer(node.get("player"));
        media.persons = new ArrayList<>();
        ((ArrayNode) node.get("persons")).elements()
                .forEachRemaining(jsonNode -> media.persons.add(jsonNode.asText()));
        return media;
    }


    public Player readPlayer(TreeNode node) {
        return node == null ? null : Player.find(((JsonNode) node).asText());
    }

    public Size readSize(JsonNode node) {
        return node == null ? null : Size.valueOf(node.asText());
    }

    public Image readImage(JsonNode node) {
        Image image = new Image();
        JsonNode uriNode = node.get("uri");
        image.uri = uriNode == null ? null : uriNode.asText();
        JsonNode titleNode = node.get("title");
        image.title = titleNode == null ? null : titleNode.asText();
        IntNode wNode = (IntNode)node.get("width");
        image.width = wNode == null ? 0 : (Integer) wNode.numberValue();
        IntNode hNode = (IntNode)node.get("height");
        image.height = hNode == null ? 0 : (Integer) hNode.numberValue();
        JsonNode sizeNode = node.get("size");
        image.size = sizeNode == null ? null : readSize(sizeNode);
        return image;
    }
}
