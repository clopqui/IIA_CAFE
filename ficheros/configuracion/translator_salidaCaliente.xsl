<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">
	<xsl:template match=\"/drink\">
		<sql>SELECT `name` FROM `bebidas_calientes` WHERE `name` = '<xsl:value-of select=\"name\"/>'</sql>
	</xsl:template>
</xsl:stylesheet>