package com.suifeng.javaparsertool.support;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.NamespaceStack;
import org.xml.sax.Attributes;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;

/**
 * 继承XMLWriter，重新排版Attributes
 */
public class MyXmlWriter extends XMLWriter {

    private OutputFormat format;

    public MyXmlWriter(Writer writer) {
        super(writer);
    }

    public MyXmlWriter(Writer writer, OutputFormat format) {
        super(writer, format);
    }

    public MyXmlWriter() {
    }

    public MyXmlWriter(OutputStream out) throws UnsupportedEncodingException {
        super(out);
    }

    public MyXmlWriter(OutputStream out, OutputFormat format) throws UnsupportedEncodingException {
        super(out, format);
        this.format = format;
    }

    public MyXmlWriter(OutputFormat format) throws UnsupportedEncodingException {
        super(format);
    }

    @Override
    protected void writeAttribute(Attributes attributes, int index) throws IOException {
        super.writeAttribute(attributes, index);
    }

    /**
     * 重写XMLWriter的方法，当有多个Attribute时每个都换行
     * @param element
     * @throws IOException
     */
    @Override
    protected void writeAttributes(Element element) throws IOException {
        NamespaceStack namespaceStack = null;
        try {
            Field nss = this.getClass().getSuperclass().getDeclaredField("namespaceStack");
            nss.setAccessible(true);
            namespaceStack = (NamespaceStack) nss.get(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int i = 0;

        for (int size = element.attributeCount(); i < size; ++i) {
            Attribute attribute = element.attribute(i);
            Namespace ns = attribute.getNamespace();
            String attName;
            String uri;
            if (ns != null && ns != Namespace.NO_NAMESPACE && ns != Namespace.XML_NAMESPACE) {
                attName = ns.getPrefix();
                uri = namespaceStack.getURI(attName);
                if (!ns.getURI().equals(uri)) {
                    this.writeNamespace(ns);
                    namespaceStack.push(ns);
                }
            }
            attName = attribute.getName();
            if (attName.startsWith("xmlns:")) {
                String prefix = attName.substring(6);
                if (namespaceStack.getNamespaceForPrefix(prefix) == null) {
                    String uriStr = attribute.getValue();
                    namespaceStack.push(prefix, uriStr);
                    writeNamespace(prefix, uriStr);
                }
            } else if (attName.equals("xmlns")) {
                if (namespaceStack.getDefaultNamespace() == null) {
                    uri = attribute.getValue();
                    namespaceStack.push((String) null, uri);
                    this.writeNamespace((String) null, uri);
                }
            } else {
                char quote = this.format.getAttributeQuoteCharacter();
                if (size > 1) {
                    writePrintln();
                    writeIndent();
                }
                this.writer.write(" ");
                this.writer.write(attribute.getQualifiedName());
                this.writer.write("=");
                this.writer.write(quote);
                this.writeEscapeAttributeEntities(attribute.getValue());
                this.writer.write(quote);
//                if (i == size - 1) {
//                    writePrintln();
//                    writeIndent();
//                }
            }
        }
    }

    private void writeIndent() throws IOException {
        int indentLevel = 0;
        try {
            Field il = this.getClass().getSuperclass().getDeclaredField("indentLevel");
            il.setAccessible(true);
            indentLevel = (int) il.get(this) + 1;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        String indent = this.format.getIndent();
        if (indent != null && indent.length() > 0) {
            for (int i = 0; i < indentLevel; ++i) {
                this.writer.write(indent);
            }
        }

    }

    @Override
    protected void writeAttribute(Attribute attribute) throws IOException {
        super.writeAttribute(attribute);
    }

    @Override
    protected void writeAttributes(Attributes attributes) throws IOException {
        super.writeAttributes(attributes);
    }
}
