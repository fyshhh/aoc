function partone()
    total = 0
    io = open("y2020/day2/input.in")
    for line in eachline(io)
        m = match(r"(?<min>.+)-(?<max>.+) (?<char>.+): (?<password>.+)", line)
        count = 0
        c = m[ :char][1]
        for char in m[ :password]
            if c == char
                count += 1
            end
        end
        if count >= parse(Int64, m[ :min]) && count <= parse(Int64, m[ :max])
            total += 1
        end
    end
    println(total)
    return nothing
end

function parttwo()
    total = 0
    io = open("y2020/day2/input.in")
    for line in eachline(io)
        m = match(r"(?<min>.+)-(?<max>.+) (?<char>.+): (?<password>.+)", line)
        c = m[ :char][1]
        if xor(m[ :password][parse(Int64, m[ :min])] == c,
            m[ :password][parse(Int64, m[ :max])] == c)
            total += 1
        end
    end
    println(total)
    return nothing
end

function main()
    partone()
    parttwo()
end

main()
