<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">
	<xsl:template match=\"/Resultados\">
		<drink>
			<name>
				<xsl:value-of select=\"Columnas/name\"/>
			</name>
		</drink>
	</xsl:template>
</xsl:stylesheet>