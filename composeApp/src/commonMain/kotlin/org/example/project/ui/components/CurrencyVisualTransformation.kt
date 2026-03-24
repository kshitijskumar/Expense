package org.example.project.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import org.example.project.util.CurrencyUtil.INR_CURRENCY_SYMBOL

class CurrencyVisualTransformation(
    private val currencySymbol: String = "$INR_CURRENCY_SYMBOL "
) : VisualTransformation {
    
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        
        // If empty, don't add prefix
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }
        
        // Add currency symbol as prefix
        val transformedText = currencySymbol + originalText
        
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Offset in original text maps to offset + prefix length in transformed text
                return offset + currencySymbol.length
            }
            
            override fun transformedToOriginal(offset: Int): Int {
                // Offset in transformed text maps back to original
                // If cursor is in the prefix area, move it to the start of actual text
                return when {
                    offset <= currencySymbol.length -> 0
                    else -> offset - currencySymbol.length
                }
            }
        }
        
        return TransformedText(
            text = AnnotatedString(transformedText),
            offsetMapping = offsetMapping
        )
    }
}
