package pro.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pro.dao.Book;

@Configuration
public class ConfigProcessor {
	Logger log = LoggerFactory.getLogger(ConfigProcessor.class);


	@Bean
	public ItemProcessor<Book, Book> xmlProcessor() {
		return book -> book;
	}
	
	@Bean
	public ItemProcessor<Book, Book> dBProcessor() {
		return book -> {
			if(book.getGenre().equalsIgnoreCase("computer"))
				return book;
			return null;
		};
	}
}
