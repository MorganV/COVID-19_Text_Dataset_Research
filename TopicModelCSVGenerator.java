/*  TopicModelCSVGenerator.java
 *      Morgan VandenBerg
 *      mvandenberg@smu.edu
 *      SMU AI Lab
 *
 *   Used to generate a giant CSV from millions of individual topic model files.
 *  NOTE: almost all users should use the threaded version of this code instead.
 *      -The exception is if you have a high-performance disk system that will
 *       cause the code to be CPU-bound instead of read-speed-bound
 *
 */

import java.io.*;

public class TopicModelCSVGenerator {
    // Change the input directory to the root for the topic model files generation
    public final static INPUT_DIR ="../scratch/CORD19_Topic_Models/";

    public static class TopicModel {
        String[] words;
        double[] weights;
    }

    public static class TopicModelSet {
        int documentNumber;
        TopicModel[] models;
    }


    public static void main(String[] args) throws Exception {

        String line;
        TopicModelSet[] models = new TopicModelSet[500_000];
        System.out.println("Allocated mem.");

        for (int docNum = 0; docNum < 500_000; docNum++) {
            TopicModel[] docTopics = new TopicModel[15];
            for (int topicNum = 0; topicNum < 15; topicNum++) {
                File f = new File(INPUT_DIR + "_model_doc_" + docNum + "_" + topicNum + ".topic");
                if (!f.exists()) continue;
                BufferedReader bR = new BufferedReader(new FileReader(f));
                int counter = 0;
                String[] words = new String[10];
                double[] weights = new double[10];
                while ((line = bR.readLine()) != null) {
                    words[counter] = line.substring(0, line.indexOf(' '));
                    weights[counter] = Double.parseDouble(line.substring(line.indexOf(' ') + 1));
                    counter++;
                }
                bR.close();
                docTopics[topicNum] = new TopicModel();
                docTopics[topicNum].words = words;
                docTopics[topicNum].weights = weights;
            }
            if (docTopics[0] != null) {
                // means that the topics existed
                models[docNum] = new TopicModelSet();
                models[docNum].models = docTopics;
                models[docNum].documentNumber = docNum;
            }
            if (docNum % 1000 == 0) System.out.println("Read " + docNum);
        }


        System.out.println("Finished reading. Writing output.");
        BufferedWriter bW = new BufferedWriter(new FileWriter(new File("topic_models.csv")));
        bW.write("doc_id,topic_id,word,weight");
        bW.newLine();
        for (int docID = 0; docID < 500000; docID++) {
            if (models[docID] != null) {
                for (int topicID = 0; topicID < 15; topicID++) {
                    int curPos = 0;
                    for (String word : models[docID].models[topicID].words) {
                        bW.write(docID + "," + topicID + "," + word + "," + models[docID].models[topicID].weights[curPos]);
                        bW.newLine();
                        curPos++;
                    }
                }
            }
            if (docID % 1000 == 0) System.out.println("Wrote " + docID);
        }
        bW.close();
        System.out.println("Done writing.");
    }
}

