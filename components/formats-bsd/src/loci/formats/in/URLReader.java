/*
 * #%L
 * BSD implementations of Bio-Formats readers and writers
 * %%
 * Copyright (C) 2005 - 2017 Open Microscopy Environment:
 *   - Board of Regents of the University of Wisconsin-Madison
 *   - Glencoe Software, Inc.
 *   - University of Dundee
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package loci.formats.in;

import loci.common.DataTools;
import loci.common.Location;
import loci.formats.*;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * A URL reader designed for use with opaque remote resources that should not be permanently fetched
 */
public class URLReader extends WrappedReader {

  // -- Static fields --

  /** Pattern to match child URLs */
  private static final Pattern URL_MATCHER = Pattern.compile(
          "\\p{Alnum}+(\\+\\p{Alnum}+)?://.*");

  // -- Constructor --

  /** Constructs a URL reader. */
  public URLReader() {
    super("URL", new String[]{"url"});
    helper = new ImageReader(getHelperClasses());
    suffixSufficient = true;
  }

  // -- IFormatReader methods --

  @Override
  public String[] getUsedFiles(boolean noPixels) {
    return new String[] {currentId};
  }

  // -- Internal FormatReader methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    // read the URL from the file which just contain a single line with the absolute URL
    currentId = new Location(id).getAbsolutePath();
    String url = DataTools.readFile(id).trim();
    if (!URL_MATCHER.matcher(url).matches()) {
      throw new FormatException("Invalid URL: " + url);
    }
    helper.setId(url);
    core = helper.getCoreMetadataList();
  }

}
