-- Copyright (C) ---Put here your own copyright and developer email---
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with this program.  If not, see http://www.gnu.org/licenses/.


-- BEGIN MODULEBUILDER INDEXES
ALTER TABLE llx_iscreen_iscreenobject ADD INDEX idx_iscreen_iscreenobject_rowid (rowid);
-- END MODULEBUILDER INDEXES

--ALTER TABLE llx_iscreen_iscreenobject ADD UNIQUE INDEX uk_iscreen_iscreenobject_fieldxy(fieldx, fieldy);

--ALTER TABLE llx_iscreen_iscreenobject ADD CONSTRAINT llx_iscreen_iscreenobject_fk_field FOREIGN KEY (fk_field) REFERENCES llx_iscreen_myotherobject(rowid);
