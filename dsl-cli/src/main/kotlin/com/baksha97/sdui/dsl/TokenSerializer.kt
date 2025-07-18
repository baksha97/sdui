package com.baksha97.sdui.dsl

import com.baksha97.sdui.shared.models.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/**
 * Module that registers all token serializers.
 */
val tokenSerializersModule = SerializersModule {
    polymorphic(Token::class) {
        subclass(ColumnToken::class, ColumnToken.serializer())
        subclass(RowToken::class, RowToken.serializer())
        subclass(BoxToken::class, BoxToken.serializer())
        subclass(CardToken::class, CardToken.serializer())
        subclass(TextToken::class, TextToken.serializer())
        subclass(ButtonToken::class, ButtonToken.serializer())
        subclass(SpacerToken::class, SpacerToken.serializer())
        subclass(DividerToken::class, DividerToken.serializer())
        subclass(SliderToken::class, SliderToken.serializer())
        subclass(AsyncImageToken::class, AsyncImageToken.serializer())
        subclass(LazyColumnToken::class, LazyColumnToken.serializer())
        subclass(LazyRowToken::class, LazyRowToken.serializer())
    }
    polymorphic(ContainerToken::class) {
        subclass(ColumnToken::class, ColumnToken.serializer())
        subclass(RowToken::class, RowToken.serializer())
        subclass(BoxToken::class, BoxToken.serializer())
        subclass(CardToken::class, CardToken.serializer())
        subclass(LazyColumnToken::class, LazyColumnToken.serializer())
        subclass(LazyRowToken::class, LazyRowToken.serializer())
    }
    polymorphic(InteractiveToken::class) {
        subclass(ButtonToken::class, ButtonToken.serializer())
        subclass(CardToken::class, CardToken.serializer())
        subclass(SliderToken::class, SliderToken.serializer())
        subclass(AsyncImageToken::class, AsyncImageToken.serializer())
    }
}

/**
 * JSON configuration with token serializers.
 */
val tokenJson = Json {
    prettyPrint = true
    encodeDefaults = true
    ignoreUnknownKeys = true
    serializersModule = tokenSerializersModule
}

/**
 * Serializer for the Token interface.
 * This serializer is used to serialize and deserialize Token objects to and from JSON.
 */
object TokenSerializer : KSerializer<Token> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Token")

    override fun serialize(encoder: Encoder, value: Token) {
        require(encoder is JsonEncoder) { "This serializer can only be used with JSON" }

        val jsonElement = when (value) {
            is ColumnToken -> tokenJson.encodeToJsonElement(ColumnToken.serializer(), value)
            is RowToken -> tokenJson.encodeToJsonElement(RowToken.serializer(), value)
            is BoxToken -> tokenJson.encodeToJsonElement(BoxToken.serializer(), value)
            is CardToken -> tokenJson.encodeToJsonElement(CardToken.serializer(), value)
            is TextToken -> tokenJson.encodeToJsonElement(TextToken.serializer(), value)
            is ButtonToken -> tokenJson.encodeToJsonElement(ButtonToken.serializer(), value)
            is SpacerToken -> tokenJson.encodeToJsonElement(SpacerToken.serializer(), value)
            is DividerToken -> tokenJson.encodeToJsonElement(DividerToken.serializer(), value)
            is SliderToken -> tokenJson.encodeToJsonElement(SliderToken.serializer(), value)
            is AsyncImageToken -> tokenJson.encodeToJsonElement(AsyncImageToken.serializer(), value)
            is LazyColumnToken -> tokenJson.encodeToJsonElement(LazyColumnToken.serializer(), value)
            is LazyRowToken -> tokenJson.encodeToJsonElement(LazyRowToken.serializer(), value)
            else -> throw SerializationException("Unknown token type: ${value::class}")
        }

        encoder.encodeJsonElement(jsonElement)
    }

    override fun deserialize(decoder: Decoder): Token {
        require(decoder is JsonDecoder) { "This serializer can only be used with JSON" }

        val jsonElement = decoder.decodeJsonElement()
        require(jsonElement is JsonObject) { "Expected JsonObject for Token deserialization" }

        // Determine token type from the JSON structure
        val tokenType = when {
            jsonElement.containsKey("children") -> {
                when {
                    jsonElement.containsKey("alignment") && 
                    jsonElement["alignment"]?.jsonPrimitive?.contentOrNull?.contains("Horizontal") == true -> "ColumnToken"
                    jsonElement.containsKey("alignment") && 
                    jsonElement["alignment"]?.jsonPrimitive?.contentOrNull?.contains("Vertical") == true -> "RowToken"
                    jsonElement.containsKey("contentAlignment") -> "BoxToken"
                    jsonElement.containsKey("elevation") -> "CardToken"
                    jsonElement.containsKey("reverseLayout") -> {
                        if (jsonElement.containsKey("horizontalAlignment")) "LazyColumnToken" else "LazyRowToken"
                    }
                    else -> "ColumnToken" // Default container type
                }
            }
            jsonElement.containsKey("text") && jsonElement.containsKey("onClick") -> "ButtonToken"
            jsonElement.containsKey("text") -> "TextToken"
            jsonElement.containsKey("height") && !jsonElement.containsKey("width") -> "SpacerToken"
            jsonElement.containsKey("thickness") -> "DividerToken"
            jsonElement.containsKey("initialValue") -> "SliderToken"
            jsonElement.containsKey("url") -> "AsyncImageToken"
            else -> throw SerializationException("Unable to determine token type from JSON: $jsonElement")
        }

        return when (tokenType) {
            "ColumnToken" -> tokenJson.decodeFromJsonElement(ColumnToken.serializer(), jsonElement)
            "RowToken" -> tokenJson.decodeFromJsonElement(RowToken.serializer(), jsonElement)
            "BoxToken" -> tokenJson.decodeFromJsonElement(BoxToken.serializer(), jsonElement)
            "CardToken" -> tokenJson.decodeFromJsonElement(CardToken.serializer(), jsonElement)
            "TextToken" -> tokenJson.decodeFromJsonElement(TextToken.serializer(), jsonElement)
            "ButtonToken" -> tokenJson.decodeFromJsonElement(ButtonToken.serializer(), jsonElement)
            "SpacerToken" -> tokenJson.decodeFromJsonElement(SpacerToken.serializer(), jsonElement)
            "DividerToken" -> tokenJson.decodeFromJsonElement(DividerToken.serializer(), jsonElement)
            "SliderToken" -> tokenJson.decodeFromJsonElement(SliderToken.serializer(), jsonElement)
            "AsyncImageToken" -> tokenJson.decodeFromJsonElement(AsyncImageToken.serializer(), jsonElement)
            "LazyColumnToken" -> tokenJson.decodeFromJsonElement(LazyColumnToken.serializer(), jsonElement)
            "LazyRowToken" -> tokenJson.decodeFromJsonElement(LazyRowToken.serializer(), jsonElement)
            else -> throw SerializationException("Unknown token type: $tokenType")
        }
    }
}

/**
 * Serializer for the ContainerToken interface.
 * This serializer is used to serialize and deserialize ContainerToken objects to and from JSON.
 */
object ContainerTokenSerializer : KSerializer<ContainerToken> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ContainerToken")

    override fun serialize(encoder: Encoder, value: ContainerToken) {
        require(encoder is JsonEncoder) { "This serializer can only be used with JSON" }

        val jsonElement = when (value) {
            is ColumnToken -> tokenJson.encodeToJsonElement(ColumnToken.serializer(), value)
            is RowToken -> tokenJson.encodeToJsonElement(RowToken.serializer(), value)
            is BoxToken -> tokenJson.encodeToJsonElement(BoxToken.serializer(), value)
            is CardToken -> tokenJson.encodeToJsonElement(CardToken.serializer(), value)
            is LazyColumnToken -> tokenJson.encodeToJsonElement(LazyColumnToken.serializer(), value)
            is LazyRowToken -> tokenJson.encodeToJsonElement(LazyRowToken.serializer(), value)
            else -> throw SerializationException("Unknown container token type: ${value::class}")
        }

        encoder.encodeJsonElement(jsonElement)
    }

    override fun deserialize(decoder: Decoder): ContainerToken {
        require(decoder is JsonDecoder) { "This serializer can only be used with JSON" }

        val jsonElement = decoder.decodeJsonElement()
        require(jsonElement is JsonObject) { "Expected JsonObject for ContainerToken deserialization" }

        // Determine container token type from the JSON structure
        val tokenType = when {
            jsonElement.containsKey("alignment") && 
            jsonElement["alignment"]?.jsonPrimitive?.contentOrNull?.contains("Horizontal") == true -> "ColumnToken"
            jsonElement.containsKey("alignment") && 
            jsonElement["alignment"]?.jsonPrimitive?.contentOrNull?.contains("Vertical") == true -> "RowToken"
            jsonElement.containsKey("contentAlignment") -> "BoxToken"
            jsonElement.containsKey("elevation") -> "CardToken"
            jsonElement.containsKey("reverseLayout") -> {
                if (jsonElement.containsKey("horizontalAlignment")) "LazyColumnToken" else "LazyRowToken"
            }
            else -> "ColumnToken" // Default container type
        }

        return when (tokenType) {
            "ColumnToken" -> tokenJson.decodeFromJsonElement(ColumnToken.serializer(), jsonElement)
            "RowToken" -> tokenJson.decodeFromJsonElement(RowToken.serializer(), jsonElement)
            "BoxToken" -> tokenJson.decodeFromJsonElement(BoxToken.serializer(), jsonElement)
            "CardToken" -> tokenJson.decodeFromJsonElement(CardToken.serializer(), jsonElement)
            "LazyColumnToken" -> tokenJson.decodeFromJsonElement(LazyColumnToken.serializer(), jsonElement)
            "LazyRowToken" -> tokenJson.decodeFromJsonElement(LazyRowToken.serializer(), jsonElement)
            else -> throw SerializationException("Unknown container token type: $tokenType")
        }
    }
}

/**
 * Serializer for the InteractiveToken interface.
 * This serializer is used to serialize and deserialize InteractiveToken objects to and from JSON.
 */
object InteractiveTokenSerializer : KSerializer<InteractiveToken> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InteractiveToken")

    override fun serialize(encoder: Encoder, value: InteractiveToken) {
        require(encoder is JsonEncoder) { "This serializer can only be used with JSON" }

        val jsonElement = when (value) {
            is ButtonToken -> tokenJson.encodeToJsonElement(ButtonToken.serializer(), value)
            is CardToken -> tokenJson.encodeToJsonElement(CardToken.serializer(), value)
            is SliderToken -> tokenJson.encodeToJsonElement(SliderToken.serializer(), value)
            is AsyncImageToken -> tokenJson.encodeToJsonElement(AsyncImageToken.serializer(), value)
            else -> throw SerializationException("Unknown interactive token type: ${value::class}")
        }

        encoder.encodeJsonElement(jsonElement)
    }

    override fun deserialize(decoder: Decoder): InteractiveToken {
        require(decoder is JsonDecoder) { "This serializer can only be used with JSON" }

        val jsonElement = decoder.decodeJsonElement()
        require(jsonElement is JsonObject) { "Expected JsonObject for InteractiveToken deserialization" }

        // Determine interactive token type from the JSON structure
        val tokenType = when {
            jsonElement.containsKey("text") && jsonElement.containsKey("onClick") -> "ButtonToken"
            jsonElement.containsKey("elevation") && jsonElement.containsKey("onClick") -> "CardToken"
            jsonElement.containsKey("initialValue") -> "SliderToken"
            jsonElement.containsKey("url") && jsonElement.containsKey("onClick") -> "AsyncImageToken"
            else -> throw SerializationException("Unable to determine interactive token type from JSON: $jsonElement")
        }

        return when (tokenType) {
            "ButtonToken" -> tokenJson.decodeFromJsonElement(ButtonToken.serializer(), jsonElement)
            "CardToken" -> tokenJson.decodeFromJsonElement(CardToken.serializer(), jsonElement)
            "SliderToken" -> tokenJson.decodeFromJsonElement(SliderToken.serializer(), jsonElement)
            "AsyncImageToken" -> tokenJson.decodeFromJsonElement(AsyncImageToken.serializer(), jsonElement)
            else -> throw SerializationException("Unknown interactive token type: $tokenType")
        }
    }
}
