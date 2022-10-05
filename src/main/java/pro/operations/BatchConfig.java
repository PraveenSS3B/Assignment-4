package pro.operations;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pro.dao.Book;
import pro.processors.ConfigProcessor;
import pro.readers.ConfigReader;
import pro.writers.ConfigWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ConfigReader configReader;

	@Autowired
	private ConfigWriter configWriter;

	@Autowired
	private ConfigProcessor configProcessor;

	Logger log = LoggerFactory.getLogger(BatchConfig.class);


	@Bean
	public Step xML2DBStep() throws MalformedURLException {
		log.trace("Performing Step 1");
		return stepBuilderFactory.get("step 1").<Book, Book>chunk(2).reader(configReader.readerXml())
				.processor(configProcessor.xmlProcessor()).writer(configWriter.xml2dbjdbcWriter()).build();
	}

	@Bean
	public Step dB2XMLStep() throws MalformedURLException {
		log.trace("Performing Step 2");
		return stepBuilderFactory.get("Step 2").<Book, Book>chunk(2).reader(configReader.dbReaderForXML())
				.processor(configProcessor.dBProcessor()).writer(configWriter.xmlItemWriter()).build();
	}

	@Bean
	public Job job() throws MalformedURLException {
		log.trace("Starting the Job....");
		return jobBuilderFactory.get("6 Jobs").incrementer(new RunIdIncrementer()).start(xML2DBStep()).next(dB2XMLStep())
				.build();
	}

}
