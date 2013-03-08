require 'base64'

PACKAGE_NAME = "mypackage"
CLASS_NAME = "StringsArray"
FILE_NAME = "#{CLASS_NAME}.java"

ARRAY_NAME = "STRINGS_ARRAY"
ORIG_SIZES_ARRAY_NAME = "SIZES_ARRAY"

OUTPUT_DIR_PATH = ""
OUTPUT_FILE_PATH = OUTPUT_DIR_PATH + '/' + FILE_NAME 

FILES_DIR_NAME = "avatars"
FILES_EXTENSION = "jpg"
FILES_DIR_PATH = File.join(Dir.pwd, FILES_DIR_NAME)
FILES_PATH_LIST = Dir[File.join(FILES_DIR_PATH, "*." + FILES_EXTENSION)]

def file_content(file_path, chars_per_line, tabs_per_line)
    tabs = "\t" * tabs_per_line
    result = tabs + "\""
	
	encoded_content = Base64.encode64(open(file_path, 'rb').read)
	encoded_content = encoded_content.delete("\n")
	encoded_content = encoded_content.scan(/.{1,#{chars_per_line}}/).join("\"+\n" + tabs + "\"")
		    
    result += encoded_content + "\""
end

File.open(OUTPUT_FILE_PATH, 'w') do |f|

    f.print("package #{PACKAGE_NAME};\n\n")
    f.print("public class #{CLASS_NAME} {\n")
    
        f.print("\tpublic static final String[] #{ARRAY_NAME} = {\n")
        
        FILES_PATH_LIST.each do |file_path|
            f.print(file_content(file_path, 72, 2))
            
            if file_path == FILES_PATH_LIST.last
                f.print("\n")
            else
                f.print(",\n\n")
            end
        end
        
        f.print("\t};\n\n")
		
		f.print("\tpublic static final int[] #{ORIG_SIZES_ARRAY_NAME} = {")
        
        FILES_PATH_LIST.each do |file_path|
			f.print(File.open(file_path, "rb").size.to_s)
			
			if file_path != FILES_PATH_LIST.last
				f.print(", ")
			end
        end
        
        f.print("};\n")		
    
    f.print("}\n")
    
end
