package org.xrpl.xrpl4j.model.jackson.modules;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.xrpl.xrpl4j.model.client.common.LedgerIndex;
import org.xrpl.xrpl4j.model.client.transactions.TransactionResult;
import org.xrpl.xrpl4j.model.transactions.Hash256;
import org.xrpl.xrpl4j.model.transactions.Transaction;
import org.xrpl.xrpl4j.model.transactions.TransactionType;

import java.io.IOException;
import java.util.Optional;

public class TransactionResultDeserializer<T extends Transaction> extends StdDeserializer<TransactionResult<T>> {

  protected TransactionResultDeserializer() {
    super(TransactionResult.class);
  }

  @Override
  public TransactionResult<T> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
    ObjectNode objectNode = objectMapper.readTree(jsonParser);

    JavaType javaType = objectMapper.getTypeFactory().constructType(new TypeReference<T>() {
    });
    T transaction = objectMapper.readValue(objectNode.toString(), javaType);

    LedgerIndex ledgerIndex = objectNode.has("ledger_index") ?
        LedgerIndex.of(objectNode.get("ledger_index").asText()) :
        null;
    Hash256 hash = Hash256.of(objectNode.get("hash").asText());
    String status = objectNode.has("status") ? objectNode.get("status").asText() : null;
    boolean validated = objectNode.has("validated") && objectNode.get("validated").asBoolean();

    return TransactionResult.<T>builder()
        .transaction(transaction)
        .ledgerIndex(Optional.ofNullable(ledgerIndex))
        .hash(hash)
        .status(Optional.ofNullable(status))
        .validated(validated)
        .build();
  }
}
