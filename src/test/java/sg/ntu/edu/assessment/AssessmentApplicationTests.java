package sg.ntu.edu.assessment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import sg.ntu.edu.assessment.models.Quotation;
import sg.ntu.edu.assessment.services.QuotationService;

@SpringBootTest
class AssessmentApplicationTests {

	@Test
	void contextLoads() {
		List<String> fruits = new ArrayList<String>();
		fruits.add("durian");
		fruits.add("plum");
		fruits.add("pear");

		Optional<Quotation> optQuote = QuotationService.getQuotations(fruits);
		Quotation quote = optQuote.get();

		for (String item : fruits){
			float cost = quote.getQuotation(item);
			System.out.println("A(n) " + item + " costs $" + cost + ".");
			System.out.println('\n');
		}
		//Error, Unknown items: plum
	}

}
