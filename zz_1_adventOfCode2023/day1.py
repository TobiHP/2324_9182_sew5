import re

if __name__ == '__main__':
    result = 0
    num_words = ["_", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"]

    with open("inputs/day1") as f:
        lines = f.readlines()

        for line in lines:
            line = line.strip()
            print(line)
            temp = line
            new_line = ""
            cut = line[0:5]
            for i in range(len(line)):
                # replaced_cut = ""
                used_word_len = 0
                for word in num_words:
                    replaced_cut = cut.replace(word, str(num_words.index(word)))
                    if replaced_cut != cut:
                        used_word_len = len(word)
                        break
                    replaced_cut = ""
                used_word_len = used_word_len if used_word_len != 0 else 5
                # line = line.replace(cut, replaced_cut)
                if replaced_cut != "":
                    line = line[0:i] + replaced_cut + line[i:-1]

                # print(cut)
                cut = temp[i+1:i + 6]

            print(line)
            nums = re.sub("[a-z]", "", line)
            print(nums)
            print(nums[0] + nums[-1])
            result += int(nums[0] + nums[-1])
            break

            print()

    print(result)
