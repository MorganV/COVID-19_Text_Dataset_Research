# CORD-19 Coronavirus Dataset Research Repo
Morgan VandenBerg\
SMU AI Lab\
mvandenberg@smu.edu

This repo contains code to explore and process the CORD-19 dataset on the COVID-19 virus. Please note that this is a work-in-progress project; most items here are sparsely-documented in the interest of running as many experiments as possible, as quickly as possible. Much of it is not elegant, but is instead merely functional.

The original dataset and description can be found here: https://www.kaggle.com/allen-institute-for-ai/CORD-19-research-challenge

`topic_models.csv` link (as referred to in the LDA_TopicModels notebook): https://smu.box.com/s/3t52kyb3jeeftd8q4w3zd9bl59ba6x9b

I'm also providing a NumPy array representation of all the topics to avoid having to read in and process that CSV I gave. This will save several hours of processing for calculating similarity scores (see my GitHub notebook for details). The index of the array represents the document ID, and the value is a list of topics (each being a list of word, weight tuples).

Link here: https://smu.box.com/s/8ht6y6q1tjezvi4aukb7kyvd8c5df0oe

And, if anyone wants the raw similarity scores, here are the saved NumPy arrays for those in the format of `arr[docIndex] = simScore(promptTopics, docTopics)` with three metrics (Jaccard, word-embedding, and topic-embedding):

Jaccard: https://smu.box.com/s/gcg1pnm20q2mzh6i4bjryhezrhntzxtm
Word Embeddings: https://smu.box.com/s/el8n7ezrfhp2gs3s2ghkrt2v2uohleh6
Topic Embeddings: https://smu.box.com/s/q1y19359hi8yyw2up9syz9p3dw1ot9ww
