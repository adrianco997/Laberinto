################################################################
CC     = gcc
CFLAGS = -Wall -ansi -pedantic -g
EJS    = p4_e1 p4_e2 p4_e3 p4_e3_bal #p4_e4
################################
RUNFLAGS1    = ./$< m1.txt
RUNFLAGS2    = ./$< numeros.txt
RUNFLAGS3    = ./$< puntos.txt;
RUNFLAGS3_bal= ./p4_e3_bal puntos.txt
RUNFLAGS4    = ./$< m1.txt
################################################################
OBJS      = $(patsubst %.c,%.o,$(wildcard *.c))
OBJS_p4e1 = p4_e1.o map_solver.o point.o map.o stack_fp.o functions.o queue.o
OBJS_p4e2 = p4_e2.o tree.o functions.o point.o
OBJS_p4e3 = p4_e3.o# tree.o npoint.o
OBJS_p4e4 = p4_e4.o# list.o functions.o point.o
################################################################
all: $(OBJS) $(EJS)

include makefile_ext

p4_e1: $(OBJS_p4e1)
	@echo -n ">>>>>> Creating $@   	";
	$(CC) -o p4_e1 $(OBJS_p4e1);
p4_e2: $(OBJS_p4e2)
	@echo -n ">>>>>> Creating $@   	";
	$(CC) -o p4_e2 $(OBJS_p4e2);
#p4_e3: $(OBJS_p4e3)
#	@echo -n ">>>>>> Creating $@   	";
#	$(CC) -o p4_e3 $(OBJS_p4e3);
p4_e4: $(OBJS_p4e4)
	@echo -n ">>>>>> Creating $@   	";
	$(CC) -o p4_e4 $(OBJS_p4e4);
################################################################
%.o: %.c %.h
	@echo -n ">>>>>> Creating $@   	";
	$(CC) $(CFLAGS) -c -o $@ $<;
%.o: %.c
	@echo -n "=>>>>> Creating $@   	";
	$(CC) $(CFLAGS) -c -o $@ $<;
################################################################
.PHONY: run gdb runv clear clean p4_e1 p4_e2 p4_e3 p4_e4
run:           run1   run2   run3   run3_bal#   run4#
gdb:           gdb1   gdb2   gdb3   gdb3_bal#   gdb4#
runv:         runv1  runv2  runv3  runv3_bal#  runv4#
clear: clean clear1 clear2 clear3 clear3_bal# clear4#
################################################################
run1:		p4_e1
	@echo -n ">>>>>> Running $<   	";
	$(RUNFLAGS1)
run2:		p4_e2
	@echo -n ">>>>>> Running $<   	";
	$(RUNFLAGS2)
run3:		p4_e3
	@echo -n ">>>>>> Running $<   	";
	$(RUNFLAGS3)
run3_bal:	p4_e3_bal
	@echo -n ">>>>>> Running $<   	";
	$(RUNFLAGS3_bal)
run4:		p4_e4
	@echo -n ">>>>>> Running $<   	";
	$(RUNFLAGS4)
################################
gdb1:		p4_e1
	@echo -n ">>>>>> Running gdb $<   	"; sleep 1;
	gdb -tui -tty=$(shell echo `tty` | tr 0-9 1-9 ) --args $(RUNFLAGS1)
gdb2:		p4_e2
	@echo -n ">>>>>> Running gdb $<   	"; sleep 1;
	gdb -tui -tty=$(shell echo `tty` | tr 0-9 1-9 ) --args $(RUNFLAGS2)
gdb3:		p4_e3
	@echo -n ">>>>>> Running gdb $<   	"; sleep 1;
	gdb -tui -tty=$(shell echo `tty` | tr 0-9 1-9 ) --args $(RUNFLAGS3)
gdb3_bal:	p4_e3_bal
	@echo -n ">>>>>> Running gdb $<   	"; sleep 1;
	gdb -tui -tty=$(shell echo `tty` | tr 0-9 1-9 ) --args $(RUNFLAGS3_bal)
gdb4:	p4_e4
	@echo -n ">>>>>> Running gdb $<   	"; sleep 1;
	gdb -tui -tty=$(shell echo `tty` | tr 0-9 1-9 ) --args $(RUNFLAGS4)
################################
runv1:		p4_e1
	@echo -n ">>>>>> Running $< with valgrind	";
	valgrind --leak-check=full $(RUNFLAGS1)
runv2: 		p4_e2
	@echo -n ">>>>>> Running $< with valgrind	";
	valgrind --leak-check=full $(RUNFLAGS2)
runv3: 		p4_e3
	@echo -n ">>>>>> Running $< with valgrind	";
	valgrind --leak-check=full $(RUNFLAGS3)
runv3_bal: 		p4_e3_bal
	@echo -n ">>>>>> Running $< with valgrind	";
	valgrind --leak-check=full $(RUNFLAGS3_bal)
runv4: 		p4_e4
	@echo -n ">>>>>> Running $< with valgrind	";
	valgrind --leak-check=full $(RUNFLAGS4)
################################################################
clear1: 	clean
	@echo -n ">>>>>> Eliminating p4_e1	";
	rm -f p4_e1 p4_e1.exe
clear2: 	clean
	@echo -n ">>>>>> Eliminating p4_e2	";
	rm -f p4_e2 p4_e2.exe
clear3:		clean
	@echo -n ">>>>>> Eliminating p4_e3	";
	rm -f p4_e3 p4_e3.exe
clear3_bal:	clean
	@echo -n ">>>>>> Eliminating p4_e3	";
	rm -f p4_e3_bal p4_e3_bal.exe
clear4: 	clean
	@echo -n ">>>>>> Eliminating p4_e4	";
	rm -f p4_e4 p4_e4.exe
################################
clean:
	@echo ">>>>>> Eliminating files.o and temporals";
	@rm -f ./*~    ../*~    ./*/*~    ;
	@rm -f ./*.o   ../*.o   ./*/*.o   ;
	@rm -f ./*.gch ../*.gch ./*/*.gch ;
	@rm -f ./*.gdh ../*.gdh ./*/*.gdh ;
#@rm -f ./*~    ../*~    ./*/*~    #./*/*/*~    ;
#@rm -f ./*.o   ../*.o   ./*/*.o   #./*/*/*.o   ;
#@rm -f ./*.gch ../*.gch ./*/*.gch #./*/*/*.gch ;
#@rm -f ./*.gdh ../*.gdh ./*/*.gdh #./*/*/*.gdh ;
################################################################
