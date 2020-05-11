#!/bin/bash


plugin=com.ibm.cloud:project-util-plugin:0.2.1-SNAPSHOT

rm -f ${modified_parent_pom} ${expected_parent_pom}

parent_cksum=testcase-parent-pom-cksum.txt
parent_pom=testcase-parent-pom.xml
modified_parent_pom=testcase-parent-pom2.xml
expected_parent_pom=testcase-parent-pom2-reference.xml

echo "RUNNING TEST: project-util-plugin test #1"
mvn -q ${plugin}:verify-file -Dfile_path="$PWD/${parent_pom}" -Dcksum_url="file://$PWD/${parent_cksum}" 
if [ $? -ne 0 ]; then
  echo "TEST FAILURE: project-util-plugin test #1"
else
  echo "TEST PASSED"
fi

sed 's/0.3.7/0.3.8/' $parent_pom > $modified_parent_pom

echo "RUNNING TEST: project-util-plugin test #2"
mvn -q ${plugin}:verify-file -Dfile_path="$PWD/${modified_parent_pom}" -Dcksum_url="file://$PWD/${parent_cksum}"  -Dref_url="file://$PWD/${parent_pom}"
if [ $? -eq 0 ]; then
  echo "TEST FAILURE: project-util-plugin test #2, expected mvn FAIL"
else
  if [ ! -f ${expected_parent_pom} ]; then
    echo "TEST FAILURE: project-util-plugin test #2, missing reference copy"
  else
    echo "TEST PASSED"
  fi
fi

echo "RUNNING TEST: project-util-plugin test #3"
mvn -q ${plugin}:verify-file -Dfile_path="$PWD/${modified_parent_pom}" -Dcksum_url="file://$PWD/${parent_cksum}"  -Dref_url="file://$PWD/${parent_pom}" -Dreplace_on_fail=true
if [ $? -ne 0 ]; then
  echo "TEST FAILURE: project-util-plugin test #3"
else
  sum_a=`cksum ${modified_parent_pom} | cut -d' ' -f1`
  sum_b=`cksum ${expected_parent_pom} | cut -d' ' -f1`
  if [ ! "$sum_a" == "$sum_b" ]; then
    echo "TEST FAILURE: project-util-plugin test #3, local parent pom not fixed"
  else
    echo "TEST PASSED"
  fi
fi


rm -f ${modified_parent_pom} ${expected_parent_pom}

