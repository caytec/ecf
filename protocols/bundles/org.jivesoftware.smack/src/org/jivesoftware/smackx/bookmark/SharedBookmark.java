/**
 * $Revision: 1.1 $
 * $Date: 2009/12/15 09:04:06 $
 *
 * Copyright 2003-2007 Jive Software.
 *
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jivesoftware.smackx.bookmark;

/**
 *  Interface to indicate if a bookmark is shared across the server.
 *
 * @author Alexander Wenckus
 */
public interface SharedBookmark {

    /**
     * Returns true if this bookmark is shared.
     *
     * @return returns true if this bookmark is shared.
     */
    public boolean isShared();
}
