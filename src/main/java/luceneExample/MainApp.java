package luceneExample;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class MainApp {
	IndexSearcher indexSeacher;
	private static final DocumentParser documentParser = new DocumentParser();
	private static Analyzer analyzer = new StandardAnalyzer();

	// Store the index in memory:
	private static Directory directory = new RAMDirectory();

	// private static Directory directory = FSDirectory.open("/tmp/testindex");
	public static void main(String[] args) throws Exception {
		 //PDF input
		 File pdfFile = new File("C:\\Users\\Owner\\Documents\\lucene files\\pdfDoc.pdf");
		 String pdfString = documentParser.parsePdf(pdfFile);
		 processDocument(pdfString);
		
		 //Text Input
//		 File textFile = new File ("C:\\Users\\Owner\\Documents\\lucene files\\textDoc.txt");
//		 String textString = documentParser.parseText(textFile);
//		 processDocument(textString);
		//
		// //HTML input
		// File htmlFile = new File ("C:\\Users\\Owner\\Documents\\lucene
		// files\\htmlDoc.html");
		// String htmlString = documentParser.parseHTML(htmlFile);
		//
		// //Excel input
		// File excelFile = new File ("C:\\Users\\Owner\\Documents\\lucene
		// files\\excelDoc.xlsx");
		// String excelString = documentParser.parseMSDocs(excelFile);
		//
		// //MS word input
		// File wordFile = new File("C:\\Users\\Owner\\Documents\\lucene
		// files\\msDoc.docx");
		// String wordString = documentParser.parseMSDocs(wordFile);

		// Querying
		query("f1", "pdf");
	}

	public static void processDocument(String pdfString) throws IOException, ParseException {

		// To store an index on disk, use this instead:
		// Directory directory = FSDirectory.open("/tmp/testindex");
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);

		Document doc = new Document();
		doc.add(new Field("f1", pdfString, TextField.TYPE_STORED));
		iwriter.addDocument(doc);
		iwriter.close();

	}

	public static void query(String field, String word) throws IOException, Exception {
		System.out.println("querying");
		
		// Now search the index:
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		// Parse a simple query that searches for "text":
		QueryParser parser = new QueryParser(field, analyzer);
		Query query = parser.parse(word);

		TopDocs tops = isearcher.search(query, 100);

		ScoreDoc[] hits = tops.scoreDocs;

		// Iterate through the results:
		for (ScoreDoc docFound : hits) {
			Document hitDoc = isearcher.doc(docFound.doc);
			System.out.println(hitDoc);

		}
		ireader.close();
		directory.close();
	}
}
