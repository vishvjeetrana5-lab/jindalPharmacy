/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.radiology;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.relationships.Namespaces;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Text;

/**
 *
 * @author abhishek
 */
public class DocumentFindAndReplace {

    private enum FindNReplaceFlags {
        Prepare,
        Read,
        Pass
    }

    class FindNReplaceProcessor {

        private List<Text> variableContainingTextElements;
        private int startIndexOfVariableInFirstTextElement;
        private int startIndexOfVariableInEndTextElement;
        private String variableName;
        private String oldVariableValue;
        private String newVariableValue;

        public FindNReplaceProcessor() {
            this.variableContainingTextElements = new ArrayList<>();
        }

        public List<Text> getTextElements() {
            return variableContainingTextElements;
        }

        public int getStartIndexOfVariableInFirstTextElement() {
            return startIndexOfVariableInFirstTextElement;
        }

        public void setStartIndexOfVariableInEndTextElement(int startIndexOfVariable) {
            this.startIndexOfVariableInEndTextElement = startIndexOfVariable;
        }

        public int getStartIndexOfVariableInEndTextElement() {
            return startIndexOfVariableInEndTextElement;
        }

        public void setStartIndexOfVariableInFirstTextElement(int startIndexOfVariable) {
            this.startIndexOfVariableInFirstTextElement = startIndexOfVariable;
        }

        public String getVariableName() {
            return variableName;
        }

        public void setVariableName(String variableName) {
            this.variableName = variableName;
        }

        public String getOldVariableValue() {
            return oldVariableValue;
        }

        public void setOldVariableValue(String oldVariableValue) {
            this.oldVariableValue = oldVariableValue;
        }

        public String getNewVariableValue() {
            return newVariableValue;
        }

        public void setNewVariableValue(String newVariableValue) {
            this.newVariableValue = newVariableValue;
        }

        public void processFindAndReplace() throws Exception {
            if (newVariableValue != null) {
                if (variableContainingTextElements.size() > 0) {
                    for (int i = 0; i < variableContainingTextElements.size(); i++) {
                        if (i == 0) {
                            int indexOf$ = variableContainingTextElements.get(i).getValue().indexOf("$");
                            String newValue = "";
                            if (indexOf$ >= 0) {
                                newValue = variableContainingTextElements.get(i).getValue().substring(0, indexOf$);
                                newValue += this.newVariableValue;
                                variableContainingTextElements.get(i).setValue(newValue);
                            }
                        } else if (i == variableContainingTextElements.size() - 1) {
                            variableContainingTextElements.get(i).setValue(variableContainingTextElements.get(i).getValue().substring(this.startIndexOfVariableInEndTextElement + 1));
                        } else {
                            variableContainingTextElements.get(i).setValue("");
                        }
                    }
                }
            }
        }
    }

    private WordprocessingMLPackage wordDocument;

    public DocumentFindAndReplace(WordprocessingMLPackage wordDocument) {
        this.wordDocument = wordDocument;
    }

    public void searchAndReplace(Map<String, String> searchMappings) throws Exception {
        Map<String, String> mappingsForHeaderFooter = new HashMap<>();
        Iterator<String> iterator = searchMappings.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = searchMappings.get(key);
            if (value != null) {
                mappingsForHeaderFooter.put("${" + key + "}", value);
            }
        }
        replaceWithContent(searchMappings);
        replaceWithHeaderAndFooter(mappingsForHeaderFooter);
        System.out.println(searchMappings.toString());
    }

    private void replaceWithContent(Map<String, String> searchMappings) throws Exception {
        VariablePrepare.prepare(wordDocument);
        wordDocument.getMainDocumentPart().variableReplace(searchMappings);
    }

    private void replaceWithHeaderAndFooter(Map<String, String> searchMappings) throws Exception {
        RelationshipsPart rp = wordDocument.getMainDocumentPart()
                .getRelationshipsPart();
        for (Relationship r : rp.getRelationships().getRelationship()) {
            if ((r.getType().equals(Namespaces.HEADER)) || (r.getType().equals(Namespaces.FOOTER))) {
                searchNreplaceWithTexts(getAllElementFromObject(rp.getPart(r), Text.class), searchMappings);
            }
        }
    }

    private void appendTextElementStack(Text textElement, FindNReplaceProcessor processor, int characterIndex) throws Exception {
        if (processor != null) {
            processor.setStartIndexOfVariableInEndTextElement(characterIndex);
            if (processor.getTextElements().indexOf(textElement) < 0) {
                processor.getTextElements().add(textElement);
            }
        }
    }

    private void searchNreplaceWithTexts(List<Text> listTexts, Map<String, String> mappings) throws Exception {
        List<FindNReplaceProcessor> processors = new ArrayList<>();

        FindNReplaceFlags flagMode = FindNReplaceFlags.Pass;
        StringBuilder textStack = new StringBuilder();
        FindNReplaceProcessor processor = null;

        for (int i = 0; i < listTexts.size(); i++) {
            Text textElement = listTexts.get(i);
            String textValue = textElement.getValue();
            char[] textValueChars = textValue.toCharArray();
            for (int c = 0; c < textValueChars.length; c++) {
                char textValueChar = textValueChars[c];
                switch (textValueChar) {
                    case '$':
                        flagMode = FindNReplaceFlags.Prepare;
                        textStack.delete(0, textStack.length());
                        processor = null;
                        textStack.append(textValueChar);
                        processor = new FindNReplaceProcessor();
                        processor.setStartIndexOfVariableInFirstTextElement(c);
                        appendTextElementStack(textElement, processor, c);
                        break;
                    case '{':
                        if (flagMode == FindNReplaceFlags.Prepare) {
                            flagMode = FindNReplaceFlags.Read;
                            appendTextElementStack(textElement, processor, c);
                        } else {
                            flagMode = FindNReplaceFlags.Pass;
                        }
                        textStack.append(textValueChar);
                        break;
                    case '}':
                        if (flagMode == FindNReplaceFlags.Read) {
                            flagMode = FindNReplaceFlags.Pass;
                        }
                        textStack.append(textValueChar);
                        appendTextElementStack(textElement, processor, c);
                        if (processor != null) {
                            String variableName = textStack.toString();
                            if (mappings.containsKey(variableName)) {
                                processor.setNewVariableValue(mappings.get(textStack.toString()));
                            }
                            processor.setVariableName(variableName);
                            processors.add(processor);
                        }
                        processor = null;
                        textStack.delete(0, textStack.length());
                        break;
                    default:
                        if (flagMode == FindNReplaceFlags.Read) {
                            textStack.append(textValueChar);
                            appendTextElementStack(textElement, processor, c);
                        } else if (flagMode == FindNReplaceFlags.Prepare) {
                            textStack.delete(0, textStack.length());
                            flagMode = FindNReplaceFlags.Pass;
                        }
                        break;
                }
            }
        }

        for (int i = 0; i < processors.size(); i++) {
            processors.get(i).processFindAndReplace();
        }

        System.out.println(processors);
    }

    private <T> List<T> getAllElementFromObject(Object obj, Class<T> toSearch) throws Exception {
        List<T> result = new ArrayList<>();
        if (obj instanceof JAXBElement) {
            obj = ((JAXBElement<?>) obj).getValue();
        }

        if (obj.getClass().equals(toSearch)) {
            result.add((T) obj);
        } else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }

        }
        return result;
    }

    public void saveDocumentToFilePath(String target) throws Exception {
        File f = new File(target);
        saveDocumentToFilePath(f);
    }

    public void saveDocumentToFilePath(File target) throws Exception {
        this.wordDocument.save(target);
    }
}
