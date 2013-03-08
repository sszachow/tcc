PACKAGE_NAME = "mypackage"
CLASS_NAME = "ImagesArray"
FILE_NAME = "#{CLASS_NAME}.java"

ARRAY_NAME = "IMAGES_ARRAY"

OUTPUT_DIR_PATH = ""
OUTPUT_FILE_PATH = OUTPUT_DIR_PATH + '/' + FILE_NAME 

FILES_DIR_NAME = "avatars"
FILES_EXTENSION = "png"
FILES_DIR_PATH = File.join(Dir.pwd, FILES_DIR_NAME)
FILES_PATH_LIST = Dir[File.join(FILES_DIR_PATH, "*." + FILES_EXTENSION)]

def file_content(file_path, bytes_per_line, tabs_per_line)
    tabs = "\t" * tabs_per_line
    result = tabs + "{"

    File.open(file_path, "rb") do |f|
        until f.eof?
            byte = f.read(1).unpack('C')[0]
            if byte > 127
                result += "(byte)0x" + byte.to_s(16)
            else
                result += "0x" + byte.to_s(16)
            end

            if f.pos != f.size
                if f.pos%bytes_per_line == 0
                    result += ",\n" + tabs
                else
                    result += ', '
                end
            else
                result += '}'
            end
            
        end
    end
    
    result
end

File.open(OUTPUT_FILE_PATH, 'w') do |f|

    f.print("package #{PACKAGE_NAME};\n\n")
    f.print("public class #{CLASS_NAME} {\n")
    
        f.print("\tpublic static final byte[][] #{ARRAY_NAME} = {\n")
        
        FILES_PATH_LIST.each do |file_path|
            f.print(file_content(file_path, 10, 2))
            
            if file_path == FILES_PATH_LIST.last
                f.print("\n")
            else
                f.print(",\n\n")
            end
        end
        
        f.print("\t};\n")
    
    f.print("}\n")
    
end
