package sg.ntu.edu.assessment.controllers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import sg.ntu.edu.assessment.models.Quotation;
import sg.ntu.edu.assessment.services.QuotationService;

@RestController
public class PurchaseOrderRestController{

    @PostMapping(path = "/api/po")
    public ResponseEntity<String> orderData(@RequestBody String formData) {
        System.out.println("form data >>" + formData);
        
        JsonReader jsonReader = Json.createReader(new StringReader(formData));
        JsonObject formJson = jsonReader.readObject();
        JsonArray lineItems = formJson.getJsonArray("lineItems");

        List<String> items = new ArrayList<String>(); 
        
        for (int i = 0; i < lineItems.size(); i++) {
            JsonObject item = lineItems.getJsonObject(i);
            items.add(item.getString("item"));
        }
                
        Optional<Quotation> optQuote = QuotationService.getQuotations(items);
        Quotation quote = optQuote.get();

        try {
            String name = formJson.getString("name");
            String invoiceId = quote.getQuoteId();
            double total = 0.0;
            
            for (int i = 0; i < lineItems.size(); i++) {
                JsonObject item = lineItems.getJsonObject(i);
                int qty = item.getInt("quantity");
                String fruit = item.getString("item");
                total += qty * quote.getQuotation(fruit);
            }

            JsonObjectBuilder orderInfo = Json.createObjectBuilder();
            orderInfo
            .add("invoiceId", invoiceId)
            .add("name", name)
            .add("total", total);

            return ResponseEntity.ok().body(orderInfo.build().toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
         return ResponseEntity.status(400).body("400 Bad Request");
    }
}