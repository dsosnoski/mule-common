package org.mule.common.metadata.builder;

import org.mule.common.metadata.DefaultXmlMetaDataModel;
import org.mule.common.metadata.XmlMetaDataModel;
import org.mule.common.metadata.XmlMetaDataNamespaceManager;
import org.mule.common.metadata.property.DescriptionMetaDataProperty;
import org.mule.common.metadata.property.LabelMetaDataProperty;
import org.mule.common.metadata.property.TextBasedExampleMetaDataModelProperty;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.io.IOUtils;
import org.mule.common.metadata.property.xml.XsiTypeMetaDataProperty;

public class DefaultXmlMetaDataBuilder<P extends MetaDataBuilder<?>> implements XmlMetaDataBuilder<P> {

    public QName name;
    public QName type;
    public List<String> schemas = new ArrayList<String>();
    public List<InputStream> schemasStream = new ArrayList<InputStream>();
    public List<URL> schemasUrls = new ArrayList<URL>();
    public Charset encoding = Charset.forName("UTF-8");
    public String example;
    private String label;
    private String description;
    private URL sourceUrl;
    private XmlMetaDataNamespaceManager namespaceManager = new XmlMetaDataNamespaceManager();

    public DefaultXmlMetaDataBuilder(String name) {
        this(new QName(name));
    }

    public DefaultXmlMetaDataBuilder(QName name) {
        this.name = name;
    }

    @Override
    public XmlMetaDataModel build()
    {

        XmlMetaDataModel model = null;

        if (schemasStream != null) {
            List<String> result = new ArrayList<String>();
            for (InputStream schemaStream : schemasStream) {
                result.add(getStringFromInputStream(schemaStream, encoding));
            }
            schemas = result;
        }

        if (type != null) {
            if (schemas != null) {
                model = new DefaultXmlMetaDataModel(schemas, sourceUrl, name, type, encoding, namespaceManager, new TextBasedExampleMetaDataModelProperty(example), new XsiTypeMetaDataProperty(type));
            }
            else if (schemasUrls != null) {
                model = new DefaultXmlMetaDataModel(schemasUrls, name, namespaceManager, new TextBasedExampleMetaDataModelProperty(example));
            }
        }
        else {
            if (schemas != null) {
                model = new DefaultXmlMetaDataModel(schemas, sourceUrl, name, encoding, namespaceManager, new TextBasedExampleMetaDataModelProperty(example));
            } else if (schemasUrls != null) {
                model = new DefaultXmlMetaDataModel(schemasUrls, name, namespaceManager, new TextBasedExampleMetaDataModelProperty(example));
            }
        }

        if (label != null)
        {
            model.addProperty(new LabelMetaDataProperty(label));
        }

        if (description != null)
        {
            model.addProperty(new DescriptionMetaDataProperty(description));
        }

        if (type!= null)
        {
            model.addProperty(new XsiTypeMetaDataProperty(type));
        }

        return model;
    }

    private static String getStringFromInputStream(InputStream is, Charset encoding)
    {
        try
        {
            if (encoding != null)
            {
                return IOUtils.toString(is, encoding.toString());
            }
            else
            {
                return IOUtils.toString(is);
            }
        }
        catch (IOException ex)
        {
            // This is not likely to happen as we are reading from memory (Strings)
            throw new RuntimeException("Failed to turn input stream into string with encoding [" + encoding + "]", ex);
        }

    }


    @Override
    public DefaultXmlMetaDataBuilder<P> setSourceUri(URL sourceUrl)
    {
        this.sourceUrl = sourceUrl;
        return this;
    }

    @Override
    public DefaultXmlMetaDataBuilder<P> addSchemaStringList(String... schemas)
    {
        this.schemas.addAll(Arrays.asList(schemas));
        this.schemasStream = null;
        this.schemasUrls = null;
        return this;
    }

    @Override
    public DefaultXmlMetaDataBuilder<P> addSchemaStreamList(InputStream... schemaStreams)
    {
        this.schemasStream.addAll(Arrays.asList(schemaStreams));
        this.schemas = null;
        this.schemasUrls = null;
        return this;
    }

    @Override
    public DefaultXmlMetaDataBuilder<P> addSchemaUrlList(URL... schemaStreams)
    {
        this.schemasUrls.addAll(Arrays.asList(schemaStreams));
        this.schemas = null;
        this.schemasStream = null;
        return this;
    }

    @Override
    public DefaultXmlMetaDataBuilder<P> setEncoding(Charset encoding)
    {
        this.encoding = encoding;
        return this;
    }

    @Override
    public DefaultXmlMetaDataBuilder<P> setExample(String xmlExample)
    {
        this.example = xmlExample;
        return this;
    }

    @Override
    public DefaultXmlMetaDataBuilder<P> setLabel(String label)
    {
        this.label = label;
        return this;
    }

    @Override
    public DefaultXmlMetaDataBuilder<P> setDescription(String description)
    {
        this.description = description;
        return this;
    }

    @Override
    public DefaultXmlMetaDataBuilder<P> setType(QName qName)
    {
        this.type = qName;
        return this;
    }

}
