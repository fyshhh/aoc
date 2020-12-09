function partone()
    instr = readlines("y2020/day8/input.in") .|> s -> s => false
    acc = 0
    ptr = 1
    while true
        if instr[ptr].second
            acc |> println
            break
        end
        instr[ptr].first |> split |> s -> begin
            if s[1] == "jmp"
                setindex!(instr, instr[ptr].first => true, ptr)
                ptr += parse(Int, s[2])
            else
                if s[1] == "acc"
                    acc += parse(Int, s[2])
                end
                setindex!(instr, instr[ptr].first => true, ptr)
                ptr += 1
            end
        end
    end
end

function parttwo()
    instr = readlines("y2020/day8/input.in") .|> s -> s => false
    for (i, v) in instr |> enumerate
        cpy = copy(instr)
        if startswith(v.first, "nop")
            cpy[i] = ("jmp" * SubString(v.first, 4)) => 0
        elseif startswith(v.first, "jmp")
            cpy[i] = ("nop" * SubString(v.first, 4)) => 0
        end
        acc = 0
        ptr = 1
        while true
            if cpy[ptr].second
                break
            elseif ptr == cpy |> length
                flush(stdout)
                acc |> println
                return nothing
            end
            cpy[ptr].first |> split |> s -> begin
                if s[1] == "jmp"
                    setindex!(cpy, cpy[ptr].first => true, ptr)
                    ptr += parse(Int, s[2])
                else
                    if s[1] == "acc"
                        acc += parse(Int, s[2])
                    end
                    setindex!(cpy, cpy[ptr].first => true, ptr)
                    ptr += 1
                end
            end
        end
    end
end

partone()
parttwo()
