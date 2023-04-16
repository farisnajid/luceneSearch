import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
// import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
// import org.json.JSONArray;
import org.json.JSONObject;


public class JsonIndexer {
    private String indexPath;

    public JsonIndexer(String indexPath) {
        this.indexPath = indexPath;
    }

    public void indexJsonFiles(String jsonDirPath) throws IOException {
        Directory indexDir = FSDirectory.open(Paths.get(indexPath));
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(indexDir, config);

        File jsonDir = new File(jsonDirPath);
        for (File file : jsonDir.listFiles()) {
            if (file.isFile() && file.getName().endsWith("review.json")) {
                indexJsonFile(file, writer);
            }
        }

        writer.close();
    }

    private void indexJsonFile(File jsonFile, IndexWriter writer) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(jsonFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                JSONObject jsonObj = new JSONObject(line);
                
                String review_id = jsonObj.getString("review_id");
                String user_id = jsonObj.getString("user_id");
                String business_id = jsonObj.getString("business_id");
                int stars_int = jsonObj.getInt("stars");
                int useful_int = jsonObj.getInt("useful");
                int funny_int = jsonObj.getInt("funny");
                int cool_int = jsonObj.getInt("cool");
                String text = jsonObj.getString("text");
                String date = jsonObj.getString("date");

                String stars = Integer.toString(stars_int);
                String useful = Integer.toString(useful_int);
                String funny = Integer.toString(funny_int);
                String cool = Integer.toString(cool_int);

                Document doc = new Document();

                doc.add(new TextField("review_id", review_id, Field.Store.YES));
                doc.add(new TextField("user_id", user_id, Field.Store.YES));
                doc.add(new TextField("business_id", business_id, Field.Store.YES));
                doc.add(new TextField("stars", stars, Field.Store.YES));
                doc.add(new TextField("useful", useful, Field.Store.YES));
                doc.add(new TextField("funny", funny, Field.Store.YES));
                doc.add(new TextField("cool", cool, Field.Store.YES));
                doc.add(new TextField("text", text, Field.Store.YES));
                doc.add(new TextField("date", date, Field.Store.YES));

                writer.addDocument(doc);
            }
        }
    }
}
