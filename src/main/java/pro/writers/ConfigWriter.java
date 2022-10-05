package pro.writers;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import pro.dao.Book;

@Configuration
public class ConfigWriter {
	Logger log = LoggerFactory.getLogger(ConfigWriter.class);
	@Autowired
	private DataSource dataSource;


	@Bean
	public ItemWriter<Book> xmlItemWriter() {
		StaxEventItemWriter<Book> xmlFileWriter = new StaxEventItemWriter<>();

		xmlFileWriter.setResource(
				new FileSystemResource(System.getProperty("user.dir") + "/src/main/resources/output/DB2Books.xml"));

		xmlFileWriter.setRootTagName("books");

		Jaxb2Marshaller bookMarshaller = new Jaxb2Marshaller();
		bookMarshaller.setClassesToBeBound(Book.class);
		xmlFileWriter.setMarshaller(bookMarshaller);

		return xmlFileWriter;
	}

	@Bean
	public JdbcBatchItemWriter<Book> xml2dbjdbcWriter() {
		JdbcBatchItemWriter<Book> writer = new JdbcBatchItemWriter<Book>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Book>());
		writer.setSql(
				"INSERT INTO BOOK (ID, AUTHOR, DESCRIPTION, GENRE, PRICE, PUBLISH_DATE, TITLE) VALUES (:id, :author, :title, :genre, :price, :publish_date, :description)");
		writer.setDataSource(dataSource);

		return writer;
	}

}
