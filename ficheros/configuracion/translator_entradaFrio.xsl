<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output omit-xml-declaration="yes"/>
	<xsl:template match="/">
		<sql>SELECT `name` FROM `bebidas_frias` WHERE `name` = '<xsl:value-of select="drink/name"/>'</sql>

	</xsl:template>
</xsl:stylesheet>