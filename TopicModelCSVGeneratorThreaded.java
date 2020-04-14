/*  TopicModelCSVGenerator.java
 *      Morgan VandenBerg
 *      mvandenberg@smu.edu
 *      SMU AI Lab
 *
 *   Used to generate a giant CSV from millions of individual topic model files.
 *
 */


import java.io.*;

public class TopicModelCSVGeneratorThreaded {
    // Change the input directory to the root for the topic model files generation
    public static final String INPUT_DIR = "../scratch/CORD19_Topic_Models/";

    public static int read = 0;

    public static class TopicModel {
        String[] words;
        double[] weights;
    }

    public static class TopicModelSet {
        int documentNumber;
        TopicModel[] models;
    }

    public static TopicModelSet[] models = new TopicModelSet[500_000];


    public static class TopicReader extends Thread {
        int lower, upper;

        public TopicReader(int lower, int upper) {
            super();
            this.lower = lower;
            this.upper = upper;
        }

        public void run() {
            String line = null;
            for (int docNum = lower; docNum < upper; docNum++) {
                TopicModel[] docTopics = new TopicModel[15];
                for (int topicNum = 0; topicNum < 15; topicNum++) {
                    File f = new File(INPUT_DIR + "_model_doc_" + docNum + "_" + topicNum + ".topic");
                    if (!f.exists()) continue;
                    BufferedReader bR = null;
                    try {
                        bR = new BufferedReader(new FileReader(f));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    int counter = 0;
                    String[] words = new String[10];
                    double[] weights = new double[10];
                    while (true) {
                        try {
                            if (!((line = bR.readLine()) != null)) break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        words[counter] = line.substring(0, line.indexOf(' '));
                        weights[counter] = Double.parseDouble(line.substring(line.indexOf(' ') + 1));
                        counter++;
                    }
                    try {
                        bR.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                read++;
            }
        }
    }

    public static void main(String[] args) throws Exception {

        System.out.println("Listed files.");
        String line;


        System.out.println("Allocated mem.");

        TopicReader[] readers = new TopicReader[500];
        for (int i = 0; i < readers.length; i++) {
            readers[i] = new TopicReader(i * (500000 / readers.length), (i + 1) * (500000 / readers.length));
            readers[i].start();
        }

        while (read < 500000 - 500) {
            System.out.println(read);
            Thread.sleep(10000);
        }

        for (int i = 0; i < readers.length; i++) {
            readers[i].join();
            System.out.println("Joined... " + i);
        }

        System.out.println("Finished reading. Writing output.");
        BufferedWriter bW = new BufferedWriter(new FileWriter(new File("topic_models_mthread.csv")));
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
            if (docID % 100 == 0) System.out.println("Wrote " + docID);
        }
        bW.close();
        System.out.println("Done writing.");
    }
}

