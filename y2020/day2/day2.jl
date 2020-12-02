function main()
    total1 = 0
    total2 = 0
    io = open("y2020/day2/input.in")
    for line in eachline(io)
        m = match(r"(?<min>.+)-(?<max>.+) (?<char>.+): (?<password>.+)", line)
        count = 0
        c = m[ :char][1]
        str = m[ :password]
        min = parse(Int64, m[ :min])
        max = parse(Int64, m[ :max])
        for char in str
            if c == char
                count += 1
            end
        end
        if count >= min && count <= max
            total1 += 1
        end
        if xor(str[min] == c, str[max] == c)
            total2 += 1
        end
    end
    close(io)
    println(total1)
    println(total2)
    return nothing
end

main()
