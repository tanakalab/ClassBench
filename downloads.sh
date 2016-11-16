wget http://www.arl.wustl.edu/classbench/db_generator.tar.gz
wget http://www.arl.wustl.edu/classbench/trace_generator.tar.gz
wget http://www.arl.wustl.edu/classbench/parameter_files.tar.gz
tar xfvz db_generator.tar.gz
tar xfvz trace_generator.tar.gz
tar xfvz parameter_files.tar.gz
cd db_generator
make all
cd ../trace_generator
make all
cd ..
