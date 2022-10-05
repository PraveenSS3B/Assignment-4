package pro.readers;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import pro.dao.Book;
import pro.operations.BookRowMapper;

@Configuration
public class ConfigReader {

	@Autowired
	private DataSource dataSource;

	Logger log = LoggerFactory.getLogger(ConfigReader.class);

	@Bean
	public JdbcCursorItemReader<Book> dbReaderForXML() {
		JdbcCursorItemReader<Book> jdbcCursorItemReader = new JdbcCursorItemReader<Book>();
		jdbcCursorItemReader.setDataSource(dataSource);
		jdbcCursorItemReader.setSql("SELECT ID, AUTHOR, DESCRIPTION, GENRE, PRICE, PUBLISH_DATE, TITLE FROM BOOK");
		jdbcCursorItemReader.setRowMapper(new BookRowMapper());
		log.trace("Reading From Database....");
		return jdbcCursorItemReader;
	}

	@Bean
	public StaxEventItemReader<Book> readerXml() {
		StaxEventItemReader<Book> xmlFileReader = new StaxEventItemReader<Book>();

		xmlFileReader.setResource(new ClassPathResource("/input/books.xml"));
		xmlFileReader.setFragmentRootElementName("book");

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Book.class);
		xmlFileReader.setUnmarshaller(marshaller);

		return xmlFileReader;

	}

}
