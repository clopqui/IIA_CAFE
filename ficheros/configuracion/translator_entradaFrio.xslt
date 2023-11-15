<?xml version="1.0"?>
	<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output omit-xml-declaration="yes"/>


	<xsl:template match="/">
  		<name><xsl:value-of select="drink/name"/></name>
	</xsl:template>
</xsl:stylesheet>