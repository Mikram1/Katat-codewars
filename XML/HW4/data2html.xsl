<?xml version="1.0" encoding="UTF-8"?>
<!-- By Mikko Toitturi -->
<!-- The goal of this document is to transform my xml-data into an "organized" website.
	 This includes listing the breweries' countries in an ordered list, their breweries
	 and their data in a table, beer types in an unordered list done through for-each-loop,
	 and beers ordered by their breweries. 
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns="http://www.w3.org/1999/xhtml"
	version="1.0">
	<xsl:output method="html"
		version="4.0"
		encoding="UTF-8"
		indent="yes" />
		
	<xsl:param name="notePara" select="brewery_catalogue/note"/>
	
	<xsl:template match="/">
		<html>
			<head>
				<title>List of breweries</title>
			</head>
			<body>
				<xsl:call-template name="intro">
					<xsl:with-param name="notePara"/>
				</xsl:call-template>
				
				<h2>Breweries</h2>
					<table border="1">
						<thead>
							<tr>
								<th>Nationality</th>
								<th>Brewery</th>
								<th>Founding city</th>
								<th>Founding year</th>
								<th>Beers listed</th>
							</tr>
						</thead>
						<tbody>
							<xsl:apply-templates select="brewery_catalogue/countries/country/breweries/brewery"/>
						</tbody>
					</table>
					
					<p>The current beer types listed are: </p>
						<ul>
							<!-- Tässä meni pieleen jos meni. -->
							<xsl:call-template name="BeerTypes">
								<xsl:with-param name="beerNote" select="brewery_catalogue/beer_types/note"/>
							</xsl:call-template>
						</ul>
					<h2>Beers</h2>
					<p>The currently listed beers sorted by their breweries. Personal favorites are in bold.</p>
						<xsl:for-each select="brewery_catalogue/countries/country/breweries/brewery">
							<!-- The copy doesn't really do anything here, but I couldn't think of a use for it. -->
							<xsl:copy>
								<xsl:apply-templates select="beers"/>
							</xsl:copy>
						</xsl:for-each>

					<p>All beer names in a list: </p>
					<ul>
						<xsl:for-each select="brewery_catalogue/countries/country/breweries/brewery">
							<xsl:apply-templates select="beers" mode="names"/>
						</xsl:for-each>
					</ul>
					<h2>Technical stuff</h2>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template name="intro">
		<h1>Breweries, beers etc.</h1>
		
		<xsl:value-of select="$notePara"/>
		<p>This page lists different breweries and beers from them. Currently the 
		   countries are: </p>
		<ol>
			<xsl:apply-templates select="brewery_catalogue/countries/country"/>
		</ol>
		<p>More will be added in the future. Maybe.</p>
	</xsl:template>
	
	<xsl:template match="countries/country">
		<xsl:element name="li">
			<xsl:value-of select="name"/>
			<xsl:text> (</xsl:text>
			<xsl:value-of select="@countryID"/>
			<xsl:text>)</xsl:text>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="countries/country/breweries/brewery">
		<tr>
			<td>
				<xsl:value-of select="../../@countryID"/>
			</td>
			<td>
				<xsl:value-of select="name"/>
			</td>
			<td>
				<xsl:apply-templates select="founding_city"/>
			</td>
			<td>
				<xsl:value-of select="founding_year"/>
			</td>
			<td>
				<xsl:variable name="beerCount">
					<xsl:value-of select="count(beers/beer)"/>
				</xsl:variable>
				<xsl:value-of select="$beerCount"/>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template name="BeerTypes">
		<xsl:param name="beerNote" select="brewery_catalogue/beer_types/note"/>
		<xsl:value-of select="$beerNote"/>
		<xsl:for-each select="brewery_catalogue/beer_types/beer_type">
			<xsl:element name="li">
				<xsl:value-of select="name"/>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="beers">
		<xsl:element name="h3">
			<xsl:value-of select="../name"/>
		</xsl:element>
		<table>
			<thead>
				<th>Name</th>
				<th>Type</th>
				<th>Alcohol content</th>
			</thead>
			<tbody>
				<xsl:for-each select="beer">
					<tr>
						<td>
							<xsl:choose>
								<xsl:when test="@is_delicious">
									<xsl:element name="b">
										<xsl:value-of select="name"/>
									</xsl:element>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="name"/>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:value-of select="type"/>
						</td>
						<td>
							<xsl:value-of select="alcohol_content"/>
						</td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>
	
	<xsl:template match="beers" mode="names">
		<xsl:for-each select="beer">
			<xsl:element name="li">
				<xsl:value-of select="name"/>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="brewery_catalogue" mode="list">
		<p>The order of the xml-document's children are: </p>
	</xsl:template>
	
</xsl:stylesheet>
